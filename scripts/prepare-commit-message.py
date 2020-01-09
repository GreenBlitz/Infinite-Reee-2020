#!/usr/bin/env python
import sys

print("Checking commit")

with open(sys.argv[1], 'r+') as fh:
    global commit_msg
    commit_msg = fh.read()

if len(commit_msg) < 15:
    print("Commit message must be at least 15 chars long!")
    exit(1)

print("Thanks for commiting!")
