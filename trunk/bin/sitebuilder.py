#!/usr/bin/env python
"""
Uses kid and docutils to construct the website from its rst, javadoc
and emma sources

USAGE
=====

python sitebuilder.py


"""

from os import makedirs, remove as remove_file
from os.path import walk, splitext, dirname, abspath
from os.path import basename, join as join_path, exists as path_exists
from time import time, strftime, gmtime
from subprocess import Popen, call, PIPE
from glob import glob
from sys import platform, stdout, argv
from cStringIO import StringIO
from getopt import getopt

from shutil import copy as copy_file
from distutils.archive_util import make_archive
from docutils import core, frontend
from docutils.writers import html4css1

from kid import Template

# the script assumes that it is in an immediate subdirectory
# of the top-level hjb project directory

ssh_username="tbetbe"
ssh_host="shell.berlios.de"
root = dirname(dirname(abspath(__file__)))
rst_doc_root = join_path(root, "docs/rst")
site_root = join_path(root, "pub/www")
archive_prefix = join_path(root, 'hjb_web_site_')
main_page_template = join_path(root, "bin/sitepage.kid")
css_file = "hjb_v1.css"

# precompile the template
_template = Template(file=main_page_template)

class SiteBuildingError(Exception):
    def __init__(self, message):
        self.message = message
        
def real_path(path):
    if not "cygwin" == platform:
        return path
    return Popen("cygpath -w " + path, shell=True, stdout=PIPE).communicate()[0].strip().replace("\\", "\\\\")

def rebuild_java_pages():
    ant_build_xml = real_path(join_path(root, "build.xml"))
    ant_command = "ant -f " + ant_build_xml + " javadoc.to.web instrument.publish"
    print "\nrunning",  "'" + ant_command + "'"
    if call(ant_command, shell=True):
        raise SiteBuildingError, "invocation of ant failed" 
    
def remove_old_site_archives():
    def remove_old_archive(name):
        remove_file(name)
        print "... removed old archive", name
    [remove_old_archive(a) for a in glob(archive_prefix + "*.gz")]
    
def create_a_new_site_archive(root=site_root):
    print "\n...archiving entire website"
    archive_name = strftime(archive_prefix + "%Y%m%d_%H%M%S", gmtime(time()))                            
    archive_path = make_archive(archive_name,
                                "gztar",
                                site_root,
                                ".")
    print "[COMPLETED] hjb website is archived at: ", archive_path
    return archive_path
    
def rebuild_rst_pages(doc_root=rst_doc_root):
    """
    Generate the site.

    Copies the rst files to the site, then transforms them into html,
    and creates the final output file using the html and the template.
    
    """
    def safe_copy(source, target):
        """ Ensure that targets's parent directory(ies) exist"""
        if not path_exists(dirname(target)):
            makedirs(dirname(target))
        copy_file(source, target)        
    sources = find_rst_files_in(doc_root)
    outputs = [sitepath_of(f, with_extension=".rst") for f in sources]
    print "\n... converting rst pages to html"
    [safe_copy(source, output) for source, output in zip(sources, outputs)]
    [apply_template(_template, parts_of(rst), rst.replace(".rst", ".html")) for rst in outputs] 


def apply_template(template, rst_parts, outfile):
    """ Applys template to the given rest_parts."""
    [setattr(template, "rst_" + k, v) for k, v in rst_parts.items()]
    template.write(outfile,
                   encoding='utf-8',
                   output="xhtml")

    
def find_rst_files_in(root):
    """Obtain the rst files to be published.

    Returns a list of containing the full paths to each of the rst to
    be published by scanning all subdirectories in *root*.
    
    """
    def find_rst_files(acc, dirname, fnames):
        """Populate acc with rst files"""
        if '.svn' in fnames:
            fnames.remove('.svn')
        [acc.append(join_path(dirname, f)) for f in fnames if f.endswith('.rst')]                
    rst_paths = []
    walk(root, find_rst_files, rst_paths)
    return rst_paths

def sitepath_of(filename,
                with_extension=".html",
                site_root=site_root,
                rst_root=rst_doc_root):
    """Generate the path of filename on the website."""
    return splitext(filename.replace(rst_root,
                                     site_root))[0] + with_extension

def get_stylesheet_path():
    return join_path(site_root, css_file)

def update_the_css_file():
    original_css_path = join_path(rst_doc_root, css_file)
    site_css_path = join_path(site_root, css_file)
    copy_file(original_css_path, site_css_path)

def parts_of(rst_file):
    """Get the docutils parts map from rst_file."""
    return core.publish_parts(
        source = file(rst_file).read(),
        source_path = rst_file,
        destination_path = rst_file[:-4] + ".html",
        writer_name = 'html',
        settings_overrides = {
            'initial_header_level': 2,
            'stylesheet_path': get_stylesheet_path(),
            'stylesheet': None,
            'generator': True,
            'source_link': True},
        )

def send_to_server(local_path):
    remote_path = join_path("\\~/websites", basename(local_path))
    scp_command = "scp " + local_path + " " + "@".join([ssh_username, ssh_host]) + ":" + remote_path
    print "\n... sending file using '" + scp_command + "'"
    cmd = Popen(scp_command, stdin=PIPE, stdout=PIPE, shell=True)
    cmd.communicate()
    if cmd.returncode:
        print "[FAIL] send failed"
        raise SiteBuildingError, "Send of archived website to server failed"
    else:
        print "[OK] send succeeded"
        return remote_path

def deploy_on_the_web(archive_path):
    ssh_command = "ssh " + "@".join([ssh_username, ssh_host]) + " . \\~/bin/update_main_website.sh " + archive_path
    print "\n... updating website using '" + ssh_command + "'"
    cmd = Popen(ssh_command, stdin=PIPE, stdout=PIPE, shell=True)
    cmd.communicate()
    if cmd.returncode:
        print "[FAIL] website update failed"
        raise SiteBuildingError, "website update failed"
    else:
        print "[OK] website update succeeded"

class Options(object):

    def __init__(self,
                 deploy_remotely=False,
                 skip_java=False):
        self.skip_java = skip_java
        self.deploy_remotely = deploy_remotely
        
def deploy_remotely():
    args = argv[1:]

def parse_cmd_lines():
    result = Options()
    args = argv[1:]
    opts, ignored = getopt(args, '', ['skip-java', 'deploy-remotely'])
    for o, a in opts:
        if o in ("--skip-java"):
            result.skip_java = True
        if o in ("--deploy-remotely"):
            result.deploy_remotely = True
    return result
    
def main():
    options = parse_cmd_lines()
    remove_old_site_archives()
    if not options.skip_java:
        rebuild_java_pages()
    update_the_css_file()
    rebuild_rst_pages()
    if options.deploy_remotely:
        site_archive = create_a_new_site_archive()
        remote_archive = send_to_server(site_archive)
        deploy_on_the_web(remote_archive)
    
if __name__ == '__main__':
    main()
