#!/bin/bash

echo "Base Case from Variable messagedigest for $PWD"

./clean.sh

javac MessageDigestBase.java
jar cvfe example.jar MessageDigestBase MessageDigestBase.class 
rm -rf *.class