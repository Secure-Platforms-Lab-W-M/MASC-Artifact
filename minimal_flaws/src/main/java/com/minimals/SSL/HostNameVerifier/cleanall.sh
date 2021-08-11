#!/bin/bash

for i in $(pwd)/*/; do
	cd $i
	./clean.sh
	cd ..
done
