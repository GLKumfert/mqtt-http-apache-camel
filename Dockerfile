# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project JAR file into the container at /app
COPY target/mqtt-http-bridge-1.0-SNAPSHOT.jar /app/mqtt-http-bridge-1.0-SNAPSHOT.jar

# Make port 8080 available to the world outside this container
EXPOSE 1883

# Run the JAR file
ENTRYPOINT ["java", "-jar", "mqtt-http-bridge-1.0-SNAPSHOT.jar"]