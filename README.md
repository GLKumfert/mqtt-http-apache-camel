# MQTT to HTTP Bridge

This project provides a bridge between MQTT and HTTP using Apache Camel.

## Running the Application

To compile and run the application, use the following command:


```sh
mvn clean install
mvn compile exec:java -Dexec.mainClass=com.example.MqttToHttpBridge
```

## Configuration

- **HTTP Port:** 3000
- **MQTT Port:** 1883
- **MQTT Topic:** `mytopic`

These values are hardcoded in the application.