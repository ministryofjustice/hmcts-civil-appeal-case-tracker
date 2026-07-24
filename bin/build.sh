#!/bin/bash
set -e
./gradlew clean test build
docker build -t cact .