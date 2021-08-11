#!/bin/bash

usage() {
    echo "Usage: $(basename $0) [package] [pathtoADB] [emulator port number] [adb server port]..."
}

app_package=$1
pathtoADB=$2
emuPort=$3
adbPort=$4
shift

if [ -z $app_package ]; then
    usage
    exit 1
fi

ADB="$pathtoADB"
ADB_LOGCAT="$ADB -P $adbPort -s localhost:$emuPort logcat -d *:E"    # Default to Verbose
ADB_SHELL="$ADB -P $adbPort -s localhost:$emuPort shell"

find_pids() {
    pname=$1
    if [ -z $pname ]; then
        return 1
    fi

    pids=$($ADB_SHELL "for pid in \$(ls /proc | grep -e ^[0-9]); do \
        cat /proc/\$pid/cmdline 2>/dev/null | grep -q -e $pname && \
        cat /proc/\$pid/cmdline 2>/dev/null | grep -q -v ^/system/bin/sh && \
        echo \$pid; \
    done")

    if [ -z "$pids" ]; then
        return 2
    else
        echo $pids
        return 0
    fi
}

pids=$(find_pids $app_package)
if [ $? -eq 0 ]; then
    pids_pattern=""
    for pid in $pids; do
        pid=$(echo $pid | tr -d '\r')
        pids_pattern="$pids_pattern\|([[:space:]]*$pid)"
    done
    pids_pattern=$(echo $pids_pattern | cut -c 3-)
    exec $ADB_LOGCAT | grep -e $pids_pattern --color=never
else
    echo >&2 "Error: app $app_package not running"
    exit 1
fi
