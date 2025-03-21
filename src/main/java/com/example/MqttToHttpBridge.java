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
            from("paho-mqtt5:mytopic?brokerUrl=tcp://host.docker.internal:1883")
            .process(exchange -> {
                long startTime = System.nanoTime();
                exchange.setProperty("startTime", startTime);
                System.out.println("Message received: " + exchange.getIn().getBody(String.class));
            })
            .log("Received MQTT Message: ${body}")
            .setHeader("Content-Type", constant("text/plain"))
            .to("http://host.docker.internal:3000") //http send
            .process(exchange -> {
                Long startTime = exchange.getProperty("startTime", Long.class);
                if (startTime != null) {
                Long latency = (System.nanoTime() - startTime)/1000;
                System.out.println("Latency: " + latency + " µs");
                } else {
                System.out.println("Start time property is missing.");
                }
            });
            }
        });

        context.start();
        System.out.println("MQTT to HTTP bridge is running...");

        // Keep the main thread alive to let Camel routes keep running
        synchronized (MqttToHttpBridge.class) {
            MqttToHttpBridge.class.wait();
        }


        // isnt looped, just slept for 10mins ish
        Thread.sleep(1000000);
        context.stop();
    }
}
