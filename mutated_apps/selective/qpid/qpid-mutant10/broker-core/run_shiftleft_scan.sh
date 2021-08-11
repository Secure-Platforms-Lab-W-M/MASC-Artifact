#! /bin/bash

mvn clean
mvn test-compile
scan -i $PWD -t java
