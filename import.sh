#!/bin/bash -x

TRUNK="$(pwd)/v4l4j-trunk"

svn co http://v4l4j.googlecode.com/svn/v4l4j/trunk $TRUNK > svn.log

rm -rf src/main/java/au

cp $TRUNK/COPYING .
cp $TRUNK/CREDITS .
cp $TRUNK/README .
cp -r $TRUNK/src/au src/main/java

rm -rf src/main/java/au/edu/jcu/v4l4j/examples
rm -rf src/main/java/au/edu/jcu/v4l4j/test
rm -rf $TRUNK

