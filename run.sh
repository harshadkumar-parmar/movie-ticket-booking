#!/bin/bash

# Build the project
echo "Building the project..."
mvn clean package -DskipTests

docker-compose up -d