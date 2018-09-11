package com.servicebus.simple;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;


public class ConsumerVerticle extends AbstractVerticle {

    private EventBus eventBus;
    private ServiceDiscovery serviceDiscovery;
    private long lookupId;
    private long senderTimerId;
    private String hello_address;
    @Override
    public void start() throws Exception {
        super.start();
        serviceDiscovery = ServiceDiscovery.create(vertx);
        eventBus = vertx.eventBus();
        lookupId = vertx.setPeriodic(1000,l-> lookupphase());
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
                    senderTimerId = vertx.setPeriodic(200, l -> {

                        count ++;
                        JsonObject jo = new JsonObject();
                        jo.put("message","hello");
                        jo.put("ts",System.nanoTime());
                        jo.put("msg-id",count);

                        eventBus.send(hello_address,jo);
                        if (count == 200)
                            vertx.cancelTimer(senderTimerId);

                        /*if (count == 200)
                        {
                            try {
                                EventBusService.getProxy(serviceDiscovery, IHelloService.class, ar2 -> {
                                    if (ar2.succeeded()) {
                                        IHelloService service = ar2.result();
                                        jo.put("from", "RPC");
                                            ServiceDiscovery.releaseServiceObject(serviceDiscovery, service);
                                    }
                                    else
                                        System.out.println("Err.  " + ar2.cause().getMessage());


                                });
                            }catch ( Exception e) {
                                System.out.println(e);
                            }
                            finally {
                                vertx.cancelTimer(senderTimerId);
                            }

                        }*/

                    });


                } else {
                    System.out.println("we could not find a match !. retrying");
                }
            }
        });
    }


}
