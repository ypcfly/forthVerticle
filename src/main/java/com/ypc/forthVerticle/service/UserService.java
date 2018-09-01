package com.ypc.forthVerticle.service;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

import java.util.List;

@ProxyGen
public interface UserService {

    @Fluent
    UserService selectUserById(Integer id,Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    UserService selectUserConditional(String address,Integer age,Handler<AsyncResult<List<JsonObject>>> resultHandler);

    static UserService create(JDBCClient jdbcClient,Handler<AsyncResult<UserService>> readyHandler){
        return new UserServiceImpl(jdbcClient,readyHandler);
    }

    static UserServiceVertxEBProxy createProxy(Vertx vertx,String address){
        return new UserServiceVertxEBProxy(vertx,address);
    }

}
