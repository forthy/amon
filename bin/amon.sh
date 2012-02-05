#!/bin/sh

CP=target/amon-assembly-1.0-SNAPSHOT.jar

java -server -cp $CP com.github.amon.AmonStandalone $*