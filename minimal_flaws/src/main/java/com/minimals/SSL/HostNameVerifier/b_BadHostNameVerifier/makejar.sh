#!/bin/bash

echo "2BadHostNameVerifier"
./clean.sh
javac BadHostName.java ABadHostNameVerifier.java
jar cvfe BadHostName.jar BadHostName *BadHostName*.class