#! /bin/bash

for i in $(pwd)/*/; do
	cd $i
	java -jar example.jar
	cd ..
done
