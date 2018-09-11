package com.servicebus.proxy;

import com.servicebus.proxy.impl.HelloHttpService;
import com.servicebus.proxy.impl.HelloServiceVerticle;
import io.vertx.reactivex.core.Vertx;


public class MainProxy {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.rxDeployVerticle(HelloHttpService.class.getName())
                .flatMap(x -> vertx.rxDeployVerticle(HelloServiceVerticle.class.getName()))
                .flatMap(x -> vertx.rxDeployVerticle(HelloServiceConsumer.class.getName()))
                .subscribe();


    }
}
