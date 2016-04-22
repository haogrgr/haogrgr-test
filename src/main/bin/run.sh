#!/bin/bash
if test ! -e sid ; then
echo $HOSTNAME`pwd` | md5sum | awk '{print $1}' > sid;
fi
for var in $(seq 1 $1)
do
nohup java -jar -Xms2048m -Xmx2048m -XX:NewSize=1024m -XX:PermSize=512m -server -XX:+DisableExplicitGC -verbose:gc -XX:+PrintGCDateStamps -XX:+PrintGCDetails $2 >> log_`date +%s`.log &
sleep 2s
done