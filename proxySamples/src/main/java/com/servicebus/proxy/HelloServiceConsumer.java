package com.servicebus.proxy;

import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;

import io.vertx.servicediscovery.types.EventBusService;

public class HelloServiceConsumer extends AbstractVerticle {

    private EventBus eventBus;
    private ServiceDiscovery serviceDiscovery;
    private long lookupId;
    private long senderTimerId;
    private String hello_address;

    @Override
    public void start() {
        serviceDiscovery = ServiceDiscovery.create(vertx);
        eventBus = vertx.eventBus();
        lookupId = vertx.setPeriodic(1000, l -> lookupphase());
    }

    int count = 0;

    private void lookupphase() {
        serviceDiscovery.getRecord(new JsonObject().put("name", "hello-service-2"), ar -> {
            if (ar.succeeded()) {
                if (ar.result() != null) {
                    System.out.println("----------->Found the hello-service ");
                    Record r1 = ar.result();
                    vertx.cancelTimer(lookupId);
                    hello_address = ar.result().getLocation().getString("endpoint");
                    // Execute the given handler every 200 ms
                    senderTimerId = vertx.setPeriodic(1, l -> {

                        count++;
                        JsonObject jo = new JsonObject();
                        jo.put("message", "hello");
                        jo.put("command", "start");
                        jo.put("ts", System.nanoTime());
                        jo.put("msg-id", count);

                        eventBus.send("normal", jo);
                        if (count == 200) {
                            System.out.println("S-E-N-D-I-N-G now !");
                            vertx.cancelTimer(senderTimerId);
                            /*try {

                                EventBusService.getProxy(serviceDiscovery, HelloService.class, ar2 -> {
                                    if (ar2.succeeded()) {
                                        HelloService service = ar2.result();
                                        jo.put("from", "RPC");
                                        service.getGreeting(43, jo, gr -> {
                                            if (gr.succeeded()) {
                                                System.out.println("got result " + gr.result());
                                            } else {
                                                System.out.println(gr.cause().getMessage());
                                            }

                                        });
                                        ServiceDiscovery.releaseServiceObject(serviceDiscovery, service);
                                    } else
                                        System.out.println("Err.  " + ar2.cause().getMessage());


                                });
                            } catch (Exception e) {
                                System.out.println(e);
                            }*/
                            sendImmediateHttpRequest();
                        }

                    });


                } else {
                    System.out.println("we could not find a match !. retrying");
                }
            }
        });
    }


    private void sendImmediateHttpRequest() {
        this.serviceDiscovery.rxGetRecord(r-> r.getName().equals("greeting"))
                .map(rec->serviceDiscovery.getReference(rec))
                .map(sr->sr.getAs(WebClient.class))
                .flatMap(wc-> wc.get("/greetings/tomer").rxSend())
                .map(HttpResponse::bodyAsString)
                .subscribe(System.out::println);

    }
}
