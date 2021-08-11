#! /bin/bash

mvn clean
mvn package
rm -r reports
scan -i $PWD -t java
