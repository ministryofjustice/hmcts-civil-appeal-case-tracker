#!/bin/bash
docker run  --mount type=bind,source="$(pwd)",target=/dockerrun/  paulushc/apacheant ant -buildfile /dockerrun/ clean build
docker build -t cact .
