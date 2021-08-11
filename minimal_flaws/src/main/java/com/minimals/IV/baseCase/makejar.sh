#!/bin/bash

echo "Base Case Static IV for $PWD"

./clean.sh

javac StaticIV.java
jar cvfe example.jar StaticIV StaticIV.class 
rm -rf *.class