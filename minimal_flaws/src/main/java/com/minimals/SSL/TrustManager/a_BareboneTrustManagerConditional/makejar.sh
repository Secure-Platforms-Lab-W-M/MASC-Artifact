#!/bin/bash
echo "8.5.1BareboneTrustManagerConditional"
./clean.sh
javac BareboneTrustManagerConditional.java
jar cvfe example.jar BareboneTrustManagerConditional BareboneTrustManagerConditional*.class
rm -rf *.class