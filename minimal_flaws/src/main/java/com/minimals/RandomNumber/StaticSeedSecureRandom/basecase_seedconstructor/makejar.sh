#!/bin/bash

echo "Base case Seed Constructor for $PWD"

./clean.sh

javac SecureRand.java
jar cvfe example.jar SecureRand SecureRand.class

rm -rf *.class