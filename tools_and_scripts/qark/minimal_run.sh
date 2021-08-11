#! /bin/sh

type_path=$1

for i in $(find $type_path -name *.java); do
	name=$(basename $i .java)
	parent_dir=$(dirname $i)
	echo '```' > $parent_dir/qark_$name.md
	qark --java $i
	cp /usr/local/lib/python3.7/dist-packages/qark/report/report.html $parent_dir/qark_$name.html
	echo '```' >> $parent_dir/qark_$name.md
done
