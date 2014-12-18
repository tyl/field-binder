#!/bin/sh

echo Cloning and Installing Maddon Snapshot...
git clone https://github.com/mstahv/maddon
cd maddon 
mvn install -DskipTests
cd ..

mvn install

echo
echo
echo "You can now start the demo (remember to start mongod)"
