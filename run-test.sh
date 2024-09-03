#!/bin/bash

# Build the project
echo "Building the project..."
mvn clean test

# Run the application
echo "Running the application..."
mvn jococo:report
