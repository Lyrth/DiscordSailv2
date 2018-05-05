#!/bin/bash

JARNAME=JavaDiscordSAILv2.jar
JAVAPATH=`which java`
JAVAARGS=-Xmx1G

while true; do
	screen -dmS sail $JAVAPATH $JAVAARGS -jar $JARNAME

	if [ $? -eq 0 ] || [ $? -eq 2 ]; then
		break
	fi
done
