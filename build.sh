#!/bin/bash
docker run  --mount type=bind,source="$(pwd)",target=/dockerrun/  paulushc/apacheant ant -buildfile /dockerrun/ clean build
docker build -t cact .
echo "to run:     docker run -p 8080:8080 --name cact cact"
echo "to remove:  docker container rm cact"
echo
echo "or use docker compose up"
