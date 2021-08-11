#!/bin/bash

echo "4BadHostNameVerifierGenericCondition"
./clean.sh

javac BadHostName.java ABadHostNameVerifier.java
jar cvfe BadHostname.jar BadHostName *BadHostName*.class

rm -rf *.class
