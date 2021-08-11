#! /bin/bash

type_path=$1

for i in $type_path/*/; do
	for j in $i/*.jar; do
		echo $j
		java -jar main/build/libs/main.jar "jar" $j "" 1 > $i/crypto_guard.md
		sed -i '1s/^/```\n/' $i/crypto_guard.md
		echo '```' >> $i/crypto_guard.md
	done
	echo $i
done
