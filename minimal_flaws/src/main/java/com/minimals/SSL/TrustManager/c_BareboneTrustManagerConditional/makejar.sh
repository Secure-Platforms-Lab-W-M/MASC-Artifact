#!/bin/bash
echo "8.6 BareboneTrustManagerConditional"
./clean.sh
javac BareboneTrustManager*.java
jar cvfe example.jar BareboneTrustManagerConditional BareboneTrustManager*.class
rm -rf *.class