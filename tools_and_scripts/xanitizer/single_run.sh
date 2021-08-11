#! /bin/bash

proj_path=$1
xani_version=$2
echo $(basename $proj_path)
parent=$(dirname $proj_path)
parent=$(basename $parent)
name=$(basename $proj_path)
$(pwd)/Xanitizer-$xani_version/XanitizerHeadless rootDirectory=$proj_path projectName=$name findingsListReportOutputFile=$proj_path/reports/xani_$xani_version.v_report.html overwriteConfigFile=true
