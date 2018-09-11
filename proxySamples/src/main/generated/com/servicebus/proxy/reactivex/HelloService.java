/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.servicebus.proxy.reactivex;

import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;


@io.vertx.lang.reactivex.RxGen(com.servicebus.proxy.HelloService.class)
public class HelloService {

  @Override
  public String toString() {
    return delegate.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HelloService that = (HelloService) o;
    return delegate.equals(that.delegate);
  }
  
  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  public static final io.vertx.lang.reactivex.TypeArg<HelloService> __TYPE_ARG = new io.vertx.lang.reactivex.TypeArg<>(
    obj -> new HelloService((com.servicebus.proxy.HelloService) obj),
    HelloService::getDelegate
  );

  private final com.servicebus.proxy.HelloService delegate;
  
  public HelloService(com.servicebus.proxy.HelloService delegate) {
    this.delegate = delegate;
  }

  public com.servicebus.proxy.HelloService getDelegate() {
    return delegate;
  }

  public void getGreeting(int amount, JsonObject quote, Handler<AsyncResult<String>> resultHandler) { 
    delegate.getGreeting(amount, quote, resultHandler);
  }

  public Single<String> rxGetGreeting(int amount, JsonObject quote) { 
    return new io.vertx.reactivex.core.impl.AsyncResultSingle<String>(handler -> {
      getGreeting(amount, quote, handler);
    });
  }


  public static  HelloService newInstance(com.servicebus.proxy.HelloService arg) {
    return arg != null ? new HelloService(arg) : null;
  }
}
