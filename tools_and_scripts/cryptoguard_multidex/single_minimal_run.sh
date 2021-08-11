#! /bin/bash

jar_path=$1
out_path=$2
name=$(basename $jar_path .jar)

echo $jar_path
echo $out_path
echo $name
java -jar main/build/libs/main.jar "jar" $jar_path "" 1 > $out_path/crypto_guard_multidex_$name.md
sed -i '1s/^/```\n/' $out_path/crypto_guard_multidex_$name.md
echo '```' >> $out_path/crypto_guard_multidex_$name.md
