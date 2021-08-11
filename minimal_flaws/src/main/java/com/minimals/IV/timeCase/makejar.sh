#!/bin/bash

echo "CurrentTimeIV IV for $PWD"

./clean.sh

javac CurrentTimeIV.java
jar cvfe example.jar CurrentTimeIV CurrentTimeIV.class
rm -rf *.class