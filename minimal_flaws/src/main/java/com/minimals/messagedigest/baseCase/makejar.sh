#!/bin/bash

echo "Base Case Static messagedigest for $PWD"

./clean.sh

javac MessageDigestBase.java
jar cvfe example.jar MessageDigestBase MessageDigestBase.class 
rm -rf *.class