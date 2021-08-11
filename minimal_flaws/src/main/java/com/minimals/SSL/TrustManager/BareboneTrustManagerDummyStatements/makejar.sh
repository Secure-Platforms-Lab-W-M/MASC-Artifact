#!/bin/bash
echo "BareboneTrustManagerConditional"
rm -rf *.jar
javac BareboneTrustManagerConditional.java
jar cvfe example.jar BareboneTrustManagerConditional BareboneTrustManagerConditional*.class
rm -rf *.class