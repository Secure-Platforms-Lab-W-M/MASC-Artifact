#! /bin/bash

jar_path=$1
out_path=$2
name=$(basename $jar_path .jar)

echo $jar_path
echo $out_path
echo $name

java -cp CryptoAnalysis-2.0-jar-with-dependencies.jar -Xms4g -Xmx4g -Xss60m crypto.HeadlessCryptoScanner --rulesDir=$(pwd)/jca_rules --applicationCp=$jar_path > $out_path/crysl_$name.md
sed -i '1s/^/```\n/' $out_path/crysl_$name.md
echo '```' >> $out_path/crysl_$name.md
