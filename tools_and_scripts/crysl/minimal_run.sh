#! /bin/sh

type_path=$1

for i in $(find $type_path -name example.jar); do
	parent_dir=$(dirname $i)
	echo '```' > $parent_dir/crysl.md
	java -cp CryptoAnalysis-2.0-jar-with-dependencies.jar crypto.HeadlessCryptoScanner --rulesDir=$(pwd)/jca_rules --applicationCp=$i >> $parent_dir/crysl.md
	echo '```' >> $parent_dir/crysl.md
done
