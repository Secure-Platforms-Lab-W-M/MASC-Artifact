#! /bin/bash

for f in $(find . -name *$1*); do
	echo $f
	root=$(echo "$f" | cut -d "/" -f2)
	echo $root
	cp $f $root/reports
done
