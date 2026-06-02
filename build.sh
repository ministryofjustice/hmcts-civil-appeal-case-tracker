#!/bin/bash
set -e
docker run  --mount type=bind,source="$(pwd)",target=/dockerrun/  perrit/apache-ant ant -buildfile /dockerrun/ clean build
docker build -t cact .
