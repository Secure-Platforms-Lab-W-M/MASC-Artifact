#!/bin/bash

echo "KeyToAlgoDES"

./clean.sh

javac CipherExample.java
jar cvfe CipherExample.jar CipherExample CipherExample.class

rm -rf *.class