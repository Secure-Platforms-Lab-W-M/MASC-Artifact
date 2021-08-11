#! /bin/sh

type_path=$1

for i in $(find $type_path -name example.jar); do
	parent_dir=$(dirname $i)
	echo '```' > $parent_dir/spotbugs.md
	java -jar $(pwd)/spotbugs-4.0.0/lib/spotbugs.jar -textui $i >> $parent_dir/spotbugs.md
	echo '```' >> $parent_dir/spotbugs.md
done
