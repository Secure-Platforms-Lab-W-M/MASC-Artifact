#!/bin/bash

echo "5BadHostNameVerifierGeneric"
./clean.sh

javac BadHostName.java ABadHostNameVerifier.java
jar cvfe BadHostname.jar BadHostName *BadHostName*.class

rm -rf *.class
