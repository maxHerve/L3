#!/bin/bash

i=$1
in=$2
out=$3

concat="\n$i\n$(cat $in)"

echo -e "$concat" | ./prog2 > $out
