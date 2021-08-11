#! /bin/sh

type_path=$1

for i in $(find $type_path -name example.jar); do
	parent_dir=$(dirname $i)
	echo '```' > $parent_dir/crypto_guard_multidex.md
	java -jar main/build/libs/main.jar "jar" $i "" 1 >> $parent_dir/crypto_guard_multidex.md
	echo '```' >> $parent_dir/crypto_guard_multidex.md
done
