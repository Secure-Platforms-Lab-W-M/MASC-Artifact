#! /bin/bash


for d in */; do
	name=$(echo "$d" | cut -d "/" -f1)
	echo $name
	indir=/media/data/muse2/tools/coverity/cov-analysis-linux64-2020.03/idirs/$name
	cov-capture --source-dir $d --dir $indir
	cov-analyze --android-security --security --dir $indir
	cov-commit-defects --port 8080 --dir $indir --host oak --stream $name --user admin --password wm1234
done
