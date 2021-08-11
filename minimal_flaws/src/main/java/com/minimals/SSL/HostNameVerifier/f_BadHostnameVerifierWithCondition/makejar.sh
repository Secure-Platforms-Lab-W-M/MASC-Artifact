#!/bin/bash

echo "5BadHostNameVerifier"
./clean.sh

javac BadHostName.java ABadHostNameVerifier.java
jar cvfe BadHostname.jar BadHostName *BadHostName*.class

rm -rf *.class
