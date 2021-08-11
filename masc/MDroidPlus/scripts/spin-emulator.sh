#!/bin/bash

SDK_PATH=$1
emuPort=$2
adbPort=$3
avdName=$4
GPU=$5

ANDROID_ADB_SERVER_PORT=$adbPort $SDK_PATH/emulator -avd $avdName -no-audio -no-window -port $emuPort -gpu $GPU &