#!/bin/bash

echo "BaseCaseInterProc"

./clean.sh

rm -rf *.jar 

javac CipherExample.java
jar cvfe example.jar CipherExample CipherExample.class

rm -rf *.class
