#! /bin/bash

type_path=$1

cd $1
mvn clean
mvn package
rm -r shiftleft_reports
scan -i $PWD -t java
mv reports shiftleft_reports
cd -
