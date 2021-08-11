#! /usr/bin/env python3

import sys
import os
import fnmatch
import re

from collections import Counter
from itertools import zip_longest

def grouper(iterable, n, fillvalue=None):
    args = [iter(iterable)] * n
    return zip_longest(*args, fillvalue=fillvalue)

print('Number of arguments:', len(sys.argv), 'arguments.')
print('Argument List:', str(sys.argv))

path=str(sys.argv[1])
pattern='*crypto_guard_unpatch*.md'
for dirpath, _, fns in os.walk(path):
	for fn in fns:
		if fnmatch.fnmatch(fn, pattern):
			with open(os.path.join(dirpath, fn), 'r') as f:
				report = f.read()

			violations = re.compile("[==*]").split(report)
			violations = list(filter(None, violations))
			violations = ''.join(violations).split('\n\n')
			print(fn)
			for v in violations:
				for line in v.split('\n'):
					if 'Violated' in line and 'Constants' in line:
						array = line.split('Constants: ')
						rule = array[0]
						findings = array[1][1:-1]
						print(rule, Counter(findings.split(', ')))
					elif '{' in line:
						array = line.split(', ')
						array = grouper(array, 2)
						array = map(str, array)
						#print('\n'.join(array))
			print()
