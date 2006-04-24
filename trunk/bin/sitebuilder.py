"""
Uses kid and docutils to construct the website from its rst source

USAGE
=====

python sitebuilder.py


"""

from os import makedirs
from os.path import walk, splitext, dirname, abspath
from os.path import join as join_path, exists as path_exists
from shutil import copy as copy_file
from docutils import core, frontend
from docutils.writers import html4css1

from kid import Template

rst_doc_root = "../docs/rst"
site_root = "../www"

main_page_template = "sitepage.html"
_template = Template(file=main_page_template)

def generate_site(doc_root=rst_doc_root):
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
    return abspath(join_path(rst_doc_root, "hjb_v1.css"))


def parts_of(rst_file):
    """Get the docutils parts map from rst_file."""
    return core.publish_parts(
        source = file(rst_file).read(),
        source_path = rst_file,
        destination_path = rst_file[:-4] + ".html",
        writer_name = 'html',
        settings_overrides = {
            'initial_header_level': 3,
            'stylesheet_path': get_stylesheet_path(),
            'stylesheet': None,
            'generator': True,
            'source_link': True},
        )


def main():
    generate_site()


if __name__ == '__main__':
    main()
