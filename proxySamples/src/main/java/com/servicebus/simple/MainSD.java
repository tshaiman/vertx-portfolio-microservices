package com.servicebus.simple;

import io.vertx.core.Vertx;

public class MainSD {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HelloSimpleService.class.getName());
        vertx.deployVerticle(ConsumerVerticle.class.getName());
    }
}
