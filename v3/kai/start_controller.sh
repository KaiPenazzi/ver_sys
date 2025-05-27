#!/bin/bash
./gradlew shadowJar
cp node/build/libs/node-all.jar node.jar
java -jar controller/build/libs/controller-all.jar localhost:10000 ../mynet.json
