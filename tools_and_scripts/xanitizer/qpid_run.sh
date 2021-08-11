#! /bin/bash

proj_path=$1
echo $(basename $proj_path)
parent=$(dirname $proj_path)
parent=$(basename $parent)
name=$(basename $proj_path)
mkdir -p $(pwd)/qpid_reach_results/$parent/$name
$(pwd)/Xanitizer-4.3.3/XanitizerHeadless rootDirectory=$proj_path projectName=$name findingsListReportOutputFile=$(pwd)/qpid_reach_results/$parent/$name/$name.pdf overwriteConfigFile=true
