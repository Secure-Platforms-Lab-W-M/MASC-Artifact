#!/bin/bash

echo "ComplexCase messagedigest for $PWD"

./clean.sh

javac MessageDigestComplex.java
jar cvfe example.jar MessageDigestComplex MessageDigestComplex.class 
rm -rf *.class