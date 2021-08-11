#! /bin/bash

for i in $(pwd)/*/; do
	cd $i
	./makejar.sh
	cd ..
done
