#! /bin/bash

path=$1
for i in $(find $path -name *.apk); do
	echo $i
	parent_dir=$(dirname $i)
	name=$(basename $i .apk)
	java -jar -Xmx4096M main/build/libs/main.jar "apk" "$i" 1 1 > $path/crypto_guard_multidex_$name.md
	sed -i '1s/^/```\n/' $path/crypto_guard_multidex_$name.md
	echo '```' >> $path/crypto_guard_multidex_$name.md
done
