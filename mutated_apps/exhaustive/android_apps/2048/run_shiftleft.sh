#! /bin/bash

./gradlew assembleDebug
scan -i $PWD -t java
