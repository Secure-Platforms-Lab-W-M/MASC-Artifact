#!/bin/bash

echo "Base Case SSLContext for $PWD"

./clean.sh

javac ContextOfSSL.java
jar cvfe example.jar ContextOfSSL ContextOfSSL.class 
rm -rf *.class