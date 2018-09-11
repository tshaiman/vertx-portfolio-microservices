package com.servicebus.proxy;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface HelloService {

    /**
     * The address on which the service is published.
     */
    String ADDRESS = "service.hello";

    void getGreeting(int amount, JsonObject quote, Handler<AsyncResult<String>> resultHandler);

    void setAmount(Handler<AsyncResult<Void>> resultHandler);

}
