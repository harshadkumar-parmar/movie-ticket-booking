# Use the official Java 17 image as a base
FROM amazoncorretto:17

# Set the working directory to /app
WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY target/transaction-0.0.1-SNAPSHOT.jar /app/

# Expose the port that the Spring Boot application will use
EXPOSE 8080

# Run the Spring Boot application when the container starts
CMD ["java", "-jar", "transaction-0.0.1-SNAPSHOT.jar"]