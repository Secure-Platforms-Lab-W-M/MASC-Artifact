#! /bin/bash

path=$1
for i in $(find $path -name '*qark*.html'); do
	echo $i
	cat $i | grep '>INFO' | sort --unique
	cat $i | grep '>WARNING' | sort --unique
	cat $i | grep '>ERROR' | sort --unique
	cat $i | grep '>VULNERABILITY' | sort --unique
	cat $i | grep 'Insecure' -A 3 | grep MainActivity.java
	echo
done
