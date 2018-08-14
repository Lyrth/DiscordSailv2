#!/bin/bash

JARPATH=../target/
JARNAME=JavaDiscordSAILv2.jar
JAVAPATH=`which java`
JAVAARGS=-Xmx1G
ERR=0

while true; do
	screen -dmSL sail sh -c '$JAVAPATH $JAVAARGS -jar $JARPATH$JARNAME; echo $?'
	ERR=`tail -1 screenlog.0`
	if [ $ERR -eq 0 ] || [ $ERR -eq 2 ]; then
		break
	fi
done
