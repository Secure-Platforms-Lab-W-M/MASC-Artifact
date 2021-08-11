#! /bin/bash

for d in */; do
	echo $d
	mv $d/reports $d/non-shiftleft-reports
done
