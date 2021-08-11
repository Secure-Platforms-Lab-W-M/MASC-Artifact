#!/bin/bash

echo "1BadHostNameVerifier"
./clean.sh

javac BadHostName.java
jar cvfe BadHostName.jar BadHostName BadHostName*.class
rm -rf *.class