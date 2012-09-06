#! /usr/bin/env python

"""generate.py -- Generate Robot Framework Maven Plugin documentation for Read The Docs

usage: generate.py

This script creates site documentation. Maven and Sphinx commands need to be in $PATH.
"""

import sys
import os
import shutil
from os.path import abspath, dirname, join
from subprocess import call

BUILD_DIR = abspath(dirname(__file__))
ROOT = join(BUILD_DIR, '..')
SITE_TARGET = join(ROOT, 'target', 'site')
STATIC_SITE = join(BUILD_DIR, '_static', 'site')


def generate():
    clean()
    orig_dir = abspath(os.curdir)
    os.chdir(ROOT)
    rc = call(['mvn', 'site'], shell=os.name == 'nt')
    if rc:
        return rc
    shutil.copytree(SITE_TARGET, STATIC_SITE)
    os.chdir(BUILD_DIR)
    rc = call(['make', 'html'], shell=os.name == 'nt')
    os.chdir(orig_dir)
    print abspath(join(BUILD_DIR, '_build', 'html', 'index.html'))
    return rc


def clean():
    if os.path.exists(STATIC_SITE):
        print 'Cleaning', STATIC_SITE
        shutil.rmtree(STATIC_SITE)


if __name__ == '__main__':
    if sys.argv[1:]:
        print __doc__
        sys.exit(1)
    try:
        import sphinx as _
    except ImportError:
        sys.exit('Generating API docs requires Sphinx')
    else:
        sys.exit(generate())
