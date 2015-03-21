#!/usr/bin/env python

import sys

__author__ = 'anton-goy'

index1 = int(sys.argv[1])
index2 = int(sys.argv[2])

for line in sys.stdin:
    if line.startswith('"'):
        continue

    fields = line.split(',')
    print '%s\t%s' % (fields[index1], fields[index2])