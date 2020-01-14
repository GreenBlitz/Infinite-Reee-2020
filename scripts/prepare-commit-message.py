#!/usr/bin/env python
import sys
import re

print("Checking commit...")

with open(sys.argv[1], 'r+') as fh:
    global commit_msg
    commit_msg = fh.read()

if len(commit_msg) < 20:
    print("Commit message must be at least 20 chars long!")
    exit(1)

