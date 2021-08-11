#! /bin/bash

path=$1
for i in $(find $path -name *.java); do
	echo $i
	qark --java $i
	name=$(basename $i .java)
	parent=$(dirname $i)
	parent=$(basename $parent)
	cp $(pwd)/venv/lib/python3.5/site-packages/qark/report/report.html $path/qark_$parent.html
done
