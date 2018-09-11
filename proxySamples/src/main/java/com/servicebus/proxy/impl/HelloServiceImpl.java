package com.servicebus.proxy.impl;

import com.servicebus.proxy.HelloService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Vertx;
import io.vertx.servicediscovery.ServiceDiscovery;

public class HelloServiceImpl implements HelloService {

    private Vertx vertx ;
    private ServiceDiscovery discovery;

    public HelloServiceImpl(Vertx vertx, ServiceDiscovery discovery) {
        this.vertx = vertx;
        this.discovery = discovery;

        vertx.eventBus().<JsonObject>consumer("normal",evt->{
           JsonObject jsonObject = evt.body();
           int msgId = jsonObject.getInteger("msg-id");
            System.out.println("Got on Address # " + msgId + " . Thread ID = " + Thread.currentThread().getId());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void getGreeting(int amount, JsonObject quote, Handler<AsyncResult<String>> resultHandler) {
        String command = quote.getString("command");
        String from = quote.getString("from");
        System.out.println("Get Greeting called .#TID" + Thread.currentThread().getId() + " . from " + from);
        String ts = String.valueOf(System.nanoTime());
        switch (command) {
            case "start" :
                resultHandler.handle(Future.succeededFuture("Start Received " + ts));
                return;
            case "stop" :
                resultHandler.handle(Future.succeededFuture("Stop Received " + ts));
                return;
        }

        resultHandler.handle(Future.failedFuture("Could not find suitable command"));
    }

    @Override
    public void setAmount(Handler<AsyncResult<Void>> resultHandler) {
        resultHandler.handle(Future.succeededFuture());
    }
}
