#!/bin/bash

echo "base case Interprocedure messagedigest for $PWD"

./clean.sh

javac MessageDigestComplex.java
jar cvfe example.jar MessageDigestComplex MessageDigestComplex.class 
rm -rf *.class