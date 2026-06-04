#!/bin/bash
set -e
./gradlew clean test war
docker build -t cact .
