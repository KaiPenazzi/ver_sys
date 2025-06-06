#!/bin/bash
./gradlew shadowJar
java -jar controller/build/libs/controller-all.jar 192.168.178.96:10000 local.json node.jar osboxes
