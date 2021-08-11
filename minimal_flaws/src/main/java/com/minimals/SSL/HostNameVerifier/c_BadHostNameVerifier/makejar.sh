#!/bin/bash

echo "3BadHostNameVerifier"
./clean.sh

javac BadHostName.java ABadHostNameVerifier.java
jar cvfe BadHostName.jar BadHostName *BadHostName*.class
rm -rf *.class