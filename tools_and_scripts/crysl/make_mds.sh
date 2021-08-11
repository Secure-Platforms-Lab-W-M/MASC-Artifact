#! /bin/bash

path=$1
for i in */*.txt; do
	name=$(dirname $i)
	echo $name
	echo '```' > $path/crysl_$name.md
	cat $i >> $path/crysl_$name.md
	echo '```' >> $path/crysl_$name.md
done
