#! /bin/bash

path=$1
for i in $(find $path -name *.apk); do
	echo $i
	qark --apk $i
	parent=$(dirname $i)
	name=$(basename $i .apk)
	cp $(pwd)/venv/lib/python3.5/site-packages/qark/report/report.html $parent/qark_$name.html
done
