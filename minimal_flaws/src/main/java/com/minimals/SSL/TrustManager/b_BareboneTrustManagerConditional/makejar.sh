#!/bin/bash
echo "BareboneTrustManagerConditional"
./clean.sh
javac BareboneTrustManagerConditional.java
jar cvfe example.jar BareboneTrustManagerConditional BareboneTrustManagerConditional*.class
rm -rf *.class