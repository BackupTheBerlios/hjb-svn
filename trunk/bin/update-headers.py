#!/usr/bin/env python
"""Updates the headers of java files with the license-header"""
import sys
import os
from os import listdir
from os.path import isdir, join, walk
from cStringIO import StringIO
from optparse import OptionParser

HJB_PATH = ".."
HJB_RST_PATH = "../docs/rst/supported-commands"
HEADER_FILE = join(HJB_PATH, "LICENSE-header.txt")
JAVA_PATHS = [join(HJB_PATH, "src"), join(HJB_PATH, "test")]
    
def update_license_headers():
    header = file(HEADER_FILE).read()
    [[update_java_file_headers(path, header, f) for path in JAVA_PATHS] for f in [remove_from_file, add_to_file]]

MASTER_TEXT = "master command list"
MASTER_LINK = "master-command-list"

def change_master_command():
    [update_master_command_list(path, update_master) for path in [HJB_RST_PATH]]

def update_master(filename):
    inputlines = file(filename, 'r').readlines()
    outputlines = []
    for line in inputlines:
        outputlines.append(line.replace(MASTER_LINK, "index").replace(MASTER_TEXT, "back to commands"))
    file(filename, 'w').writelines(outputlines)

def update_master_command_list(parent, updater):
    for path in os.listdir(parent):
        current = join(parent, path)
        print "Checking ", current
        if isdir(current) and -1 == path.find("svn"):
            update_master_command_list(parent, updater)
        elif path.endswith(".rst"):
            print "Updating master command list in ", path
            updater(current)


def update_java_file_headers(top, header, updater):
    def update_headers_in_dir(ignored, dir_path, file_names):
        """Updates the headers of java files in a directory"""
        for f in [join(dir_path, f) for f in file_names]:
            if isdir(f) or not f.endswith(".java"):
                continue
            else:
                print "Checking for header in " + f,
                updater(header, f)
                
    walk(top, update_headers_in_dir, None)
                

def remove_from_file(header, filename, checkFirst=10):
    firstLine = header.split("\n")[0].strip()
    input = file(filename, 'r')
    for i in range(checkFirst):        
        if input.readline().strip() == firstLine:
            print " : found header, ",
            while 1:
                if input.readline().strip() == "*/": break
            withoutHeader = input.read();
            input.close()
            output = file(filename, 'w')
            output.write(withoutHeader)
            output.close()
            print " : REMOVED"
            break            
    else:
        print " : header was not present, skipping"


def add_to_file(header, filename, checkFirst=10):
    firstLine = header.split("\n")[0].strip()
    input = file(filename, 'r')
    for i in range(checkFirst):        
        if input.readline().strip() == firstLine:
            print " : header is already present, skipping"
            return
    input.seek(0)
    currentText = input.read()
    input.close()
    output = file(filename, 'w')
    output.write("""/*\n%s*/\n""" % (header,))
    output.write(currentText)
    output.close()
    print " : UPDATED"
    
def main():
    update_license_headers()
    #change_master_command()

if __name__ == '__main__':
    main()
