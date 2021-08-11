#! /bin/bash

proj_path=$1
echo $(basename $proj_path)


parent=$(dirname $proj_path)
parent=$(basename $parent)
name=$(basename $proj_path)
mkdir -p $(pwd)/qpid_select_results/$parent/$name
{ time $(pwd)/Xanitizer-4.3.3/XanitizerHeadless rootDirectory=$proj_path projectName=$name findingsListReportOutputFile=$(pwd)/qpid_select_results/$parent/$name/$name.pdf overwriteConfigFile=true ; } 2> $(pwd)/qpid_select_results/$parent/time_$name.txt
