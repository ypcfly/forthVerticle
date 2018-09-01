package com.ypc.forthVerticle;

import com.ypc.forthVerticle.dataServer.DataBaseVerticle;
import com.ypc.forthVerticle.httpServer.HttpServerVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {

        Future<String> future = Future.future();
        vertx.deployVerticle(new DataBaseVerticle(),future.completer());

        future.compose(param -> {
            Future<String> httpFuture = Future.future();
            vertx.deployVerticle(HttpServerVerticle.class.getName(),
              new DeploymentOptions().setInstances(2),
              httpFuture.completer());

            return httpFuture;
        }).setHandler(ar -> {
            if (ar.succeeded()){
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }

        });

    }

}
