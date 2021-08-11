#!/bin/bash

echo "KeyToAlgoAES"

./clean.sh

javac CipherExample.java
jar cvfe CipherExample.jar CipherExample CipherExample.class

rm -rf *.class