#!/usr/bin/env bash

export CLASSPATH=".:/usr/local/lib/antlr-4.0-complete.jar:$CLASSPATH"

java -jar /usr/local/lib/antlr-4.0-complete.jar C.g4
#javac lib/*.java
#java org.antlr.v4.runtime.misc.TestRig C program -tokens example2.c
