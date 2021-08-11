#! /bin/bash

path=$1
echo $path
qark --java $path
parent=$(dirname $path)
parent=$(basename $parent)
cp $(pwd)/venv/lib/python3.5/site-packages/qark/report/report.html $path/qark_$parent.html
