#!/bin/bash

fichier="$1"
i="-1"

while [ $i != "0" ]; do
	read i
	concat="\n$i\n$(cat $fichier)"
	tmp=`echo -e "$concat" | ./prog2`
	echo "$tmp"
done
