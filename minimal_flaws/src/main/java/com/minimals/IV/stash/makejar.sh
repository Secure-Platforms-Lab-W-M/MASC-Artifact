#!/bin/bash

echo "Static IV for $PWD"

./clean.sh

javac StaticIV.java Supplier.java
jar cvfe example.jar StaticIV StaticIV.class Supplier.class

rm -rf *.class