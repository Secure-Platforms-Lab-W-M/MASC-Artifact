#! /bin/bash

jar_path=$1
out_path=$2
echo $jar_path
java -jar main/build/libs/main.jar "jar" $jar_path "" 1 > $out_path/crypto_guard.md
sed -i '1s/^/```\n/' $out_path/crypto_guard.md
echo '```' >> $out_path/crypto_guard.md
