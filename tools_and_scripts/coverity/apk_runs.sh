#! /bin/bash

path=$1

for i in $path/*/; do
	echo $i
	name=$(basename $i)
	indir=/media/data/muse2/tools/coverity/cov-analysis-linux64-2020.03/idirs/$name
	cov-capture --source-dir $i --dir $indir
	cov-analyze --android-security --security --dir $indir
	cov-commit-defects --port 8080 --dir $indir --host oak --stream $name --user admin --password wm1234
done
