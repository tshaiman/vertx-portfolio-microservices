package com.servicebus.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;

public class HelloSimpleService extends AbstractVerticle {

    private static final String address = "greetings";
    @Override
    public void start() throws Exception {
        super.start();
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        Record record = new Record()
                .setType("eventbus-service-proxy")
                .setLocation(new JsonObject().put("endpoint", address))
                .setName("hello-service")
                .setMetadata(new JsonObject().put("version", "2.1"));

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                // publication succeeded
                System.out.println("Service Hello was published successfully");

            } else {
                // publication failed
            }
        });


        vertx.eventBus().<JsonObject>consumer(address,js->{
            System.out.println("Got Json Message "  + js.body().encodePrettily());
        });


    }
}
