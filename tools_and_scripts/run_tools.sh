#! /bin/sh

flaws_path=$1
echo $flaws_path
for tool in $(ls -d */); do
	echo "Running $tool on minimals"
	cd $tool
	./minimal_run.sh ../$flaws_path/minimal
	cd ..
done
