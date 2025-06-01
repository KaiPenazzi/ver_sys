#!/bin/bash
./gradlew shadowJar
java -jar controller/build/libs/controller-all.jar localhost:10000 ../mynet.json node/build/libs/node-all.jar
