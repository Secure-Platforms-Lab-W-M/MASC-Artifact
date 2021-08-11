#!/bin/bash

echo "ComplexCase IV for $PWD"

./clean.sh

javac ComplexStaticIV.java
jar cvfe example.jar ComplexStaticIV ComplexStaticIV.class
rm -rf *.class