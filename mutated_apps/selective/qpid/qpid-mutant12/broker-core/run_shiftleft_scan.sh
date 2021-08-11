#! /bin/bash

mvn clean
mvn package
scan -i $PWD -t java
