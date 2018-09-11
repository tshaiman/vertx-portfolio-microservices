package com.servicebus.proxy.impl;

import com.servicebus.proxy.HelloService;

import io.vertx.core.AbstractVerticle;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ProxyHelper;
import io.vertx.serviceproxy.ServiceBinder;

import static com.servicebus.proxy.HelloService.ADDRESS;

public class HelloServiceVerticle extends AbstractVerticle {

    private Record record;
    private ServiceDiscovery discovery;

    @Override
    public void start() {

        ServiceDiscovery.create(vertx,discovery->{
            this.discovery = discovery;
            HelloServiceImpl service = new HelloServiceImpl(vertx,discovery);

            // Register the service proxy on the event bus
            ServiceBinder sb = new ServiceBinder(vertx);
            sb.setAddress(ADDRESS);
            sb.register(HelloService.class,service);
            //DEPRECATED : ProxyHelper.registerService(HelloService.class, vertx, service, ADDRESS);

            Record record = EventBusService.createRecord("hello-service-2", ADDRESS, HelloService.class.getName());
            discovery.publish(record, ar -> {
                if (ar.succeeded()) {
                    this.record = record;
                    System.out.println("Hello service published");

                    // Used for health check
                    vertx.createHttpServer().requestHandler(req -> req.response().end("OK")).listen(8080);
                } else {
                    ar.cause().printStackTrace();
                }
            });
        });
    }

    @Override
    public void stop() throws Exception {
        if (record != null) {
            discovery.unpublish(record.getRegistration(), v -> {});
        }
        discovery.close();
    }
}
