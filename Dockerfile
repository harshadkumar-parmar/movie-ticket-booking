# Use the official Java 17 image as a base
FROM openjdk:17-jdk-alpine

# Use the official Java 17 image as a base
FROM openjdk:17-jdk-alpine

# Install Maven

RUN apk add --no-cache maven

# Set the working directory to /app
WORKDIR /app

# Copy the pom.xml file to the working directory
COPY pom.xml .

# Copy the application.properties file to the working directory
COPY src/main/resources/application.properties .

# Copy the Maven dependencies to the working directory
COPY src/main/resources/ .

# Copy the Java source code to the working directory
COPY src/main/java .

# Compile the Java code
RUN mvn clean package

# Expose the port that the application will use
EXPOSE 8080

# Run the application when the container starts
CMD ["java", "-jar", "target/transaction-0.0.1-SNAPSHOT.jar"]