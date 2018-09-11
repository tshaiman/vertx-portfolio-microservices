package com.servicebus.proxy.impl;

import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.Record;

import static io.vertx.reactivex.CompletableHelper.toObserver;


public class HelloHttpService extends AbstractVerticle {
    private ServiceDiscovery discovery;
    private Record record;

    @Override
    public void start(Future<Void> future) throws Exception {

        this.discovery = ServiceDiscovery.create(vertx);


        // Simple HTTP API using Vert.x Web Router.
        Router router = Router.router(vertx);
        router.get("/").handler(rc -> rc.response().end("root"));
        router.get("/greetings").handler(rc -> rc.response().end("Hello world"));
        router.get("/greetings/:name").handler(rc -> rc.response().end("Hello " + rc.pathParam("name")));

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(8080)
                // When the server is ready, we publish the service
                .flatMap(this::publish)
                // Store the record, required to un-publish it
                .doOnSuccess(rec -> this.record = rec)
                .toCompletable()
                .subscribe(toObserver(future));
    }



    private Single<Record> publish(HttpServer server) {
        // 1 - Create a service record using `io.vertx.reactivex.servicediscovery.types.HttpEndpoint.createRecord`.
        // This record define the service name ("greetings"), the host ("localhost"), the server port and the root ("/")
        // TODO
        Record record = HttpEndpoint.createRecord("greeting","localhost",server.actualPort(),"/");
        // 2 - Call the rxPublish method with the created record and return the resulting single
        return this.discovery.rxPublish(record);

    }

    @Override
    public void stop(Future<Void> future) throws Exception {
        // Unregister the service when the verticle is stopped.
        // As it's an asynchronous operation, we use a `future` parameter to indicate when the operation has been
        // completed.
        discovery.rxUnpublish(record.getRegistration()).subscribe(toObserver(future));
    }
}
