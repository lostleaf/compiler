#!/bin/bash
CC=gcc

for source in `ls *.c`; do
  $CC $source -o /dev/null 1>/dev/null 2>&1
  echo "$source:	$?"
done

