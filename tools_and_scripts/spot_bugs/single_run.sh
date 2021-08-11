#! /bin/bash

jar_path=$1
out_path=$2
name=$(basename $jar_path .jar)

echo $jar_path
echo $out_path
echo $name

java -jar $(pwd)/spotbugs-4.0.0/lib/spotbugs.jar -textui $jar_path > $out_path/spotbugs_$name.md
sed -i '1s/^/```\n/' $out_path/spotbugs_$name.md
echo '```' >> $out_path/spotbugs_$name.md
