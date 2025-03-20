package com.example;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class MqttToHttpBridge {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        // Add the route that handles MQTT-to-HTTP conversion
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("paho-mqtt5:mytopic?brokerUrl=tcp://localhost:1883")
                .process(exchange -> {
                    long startTime = System.currentTimeMillis();
                    exchange.setProperty("startTime", startTime);
                    System.out.println("Message received: " + exchange.getIn().getBody(String.class));
                })
                .log("Received MQTT Message: ${body}")
                .to("http://localhost:3000") //http send
                .process(exchange -> {
                    Long startTime = exchange.getProperty("startTime", Long.class);
                    if (startTime != null) {
                        long latency = System.currentTimeMillis() - startTime;
                        System.out.println("Latency: " + latency + " ms");
                    } else {
                        System.out.println("Start time property is missing.");
                    }
                });
            }
        });

        context.start();
        System.out.println("MQTT to HTTP bridge is running...");

        // isnt looped, just slept for 10mins ish
        Thread.sleep(1000000);
        context.stop();
    }
}
