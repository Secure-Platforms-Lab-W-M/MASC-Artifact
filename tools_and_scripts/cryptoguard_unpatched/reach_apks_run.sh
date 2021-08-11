#! /bin/bash

path=$1
for i in $(find $path -name *.apk); do
	echo $i
	parent=$(dirname $i)
	name=$(basename $i .apk)
	java -jar -Xmx1024M main/build/libs/main.jar "apk" "$i" 1 1 &> $parent/crypto_guard_unpatched_$name.md
	sed -i '1s/^/```\n/' $parent/crypto_guard_unpatched_$name.md
	echo '```' >> $parent/crypto_guard_unpatched_$name.md
done
