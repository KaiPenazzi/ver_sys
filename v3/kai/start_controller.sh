#!/bin/bash
./gradlew shadowJar
java -jar controller/build/libs/controller-all.jar 127.0.0.1:10000 ../mynet.json node/build/libs/node-all.jar
