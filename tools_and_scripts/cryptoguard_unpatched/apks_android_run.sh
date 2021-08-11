#! /bin/bash

path=$1
for i in $(find $path -name *.apk); do
	echo $i
	parent_dir=$(dirname $i)
	name=$(basename $i .apk)
	java -jar -Xmx1024M main/build/libs/main.jar "apk" "$i" 1 1 > $parent_dir/crypto_guard_$name.md
	sed -i '1s/^/```\n/' $parent_dir/crypto_guard_$name.md
	echo '```' >> $parent_dir/crypto_guard_$name.md
done
