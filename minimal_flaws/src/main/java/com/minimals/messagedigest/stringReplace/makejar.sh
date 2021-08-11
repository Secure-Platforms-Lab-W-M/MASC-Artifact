#!/bin/bash

echo "String Replace messagedigest for $PWD"

./clean.sh

javac MessageDigestComplex.java
jar cvfe example.jar MessageDigestComplex MessageDigestComplex.class 
rm -rf *.class