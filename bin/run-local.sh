#!/bin/bash
set -e

DB_HOST=${DB_HOST:localhost}
DB_PORT=${DB_PORT:5433}
DB_NAME=${DB_NAME:cact}
DB_USER=${DB_USER:cact_user}
DB_PASSWORD=${DB_PASSWORD:cact_password}

./gradlew clean test build
docker build -t cact .

docker run -e DB_HOST=$DB_HOST -e DB_PORT=$DB_PORT -e DB_USER=$DB_USER -e DB_PASSWORD=$DB_PASSWORD -p 8080:8080 cact