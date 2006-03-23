#!/usr/bin/env python
"""Updates the headers of java files with the license-header"""
import sys
import os
from os import listdir
from os.path import isdir, join
from cStringIO import StringIO
from optparse import OptionParser

HJB_PATH = ".."
HEADER_FILE = join(HJB_PATH, "LICENSE-header.txt")
JAVA_PATHS = [join(HJB_PATH, "src"), join(HJB_PATH, "test")]
    
def updateLicenseHeaders():
    header = file(HEADER_FILE).read()
    [[updateJavaFileHeaders(path, header, f) for path in JAVA_PATHS] for f in [removeFromFile, addToFile]]

def updateJavaFileHeaders(parent, header, updater):
    for path in os.listdir(parent):
        current = join(parent, path)
        if isdir(current):
            updateJavaFileHeaders(current, header, updater)
        elif path.endswith(".java"):
            print "Checking for header in " + current,
            updater(header, current)

def removeFromFile(header, filename, checkFirst=10):
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


def addToFile(header, filename, checkFirst=10):
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
    updateLicenseHeaders()

if __name__ == '__main__':
    main()
