#! /bin/bash

for d in */; do
	cd $d
	./gradlew clean
	./gradlew assembleDebug > gradlew_assembleDebug_shiftleft.log
	rm -r reports
	scan -i $PWD -t java
	cd ..
done
