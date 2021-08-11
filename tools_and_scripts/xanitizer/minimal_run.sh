#! /bin/bash

path=$1
for proj in $path/*/; do
	echo $(basename $proj)
	name=$(basename $proj)
	parent=$(dirname $proj)
	parent=$(basename $parent)
	mkdir -p $(pwd)/results1/$parent/$name
	$(pwd)/Xanitizer-5.1.3/XanitizerHeadless rootDirectory=$proj projectName=$name findingsListReportOutputFile=$(pwd)/results1/$parent/$name/$name.pdf overwriteConfigFile=true
done
