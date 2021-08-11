#!/bin/bash

echo "BaseCase"

./clean.sh

javac CipherExample.java
jar cvfe example.jar CipherExample CipherExample.class

rm -rf *.class