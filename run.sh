#!/bin/bash

# Build the project
echo "Building the project..."
mvn clean package -DskipTests

# Run the application
echo "Running the application..."
java -jar target/transaction-0.0.1-SNAPSHOT.jar
