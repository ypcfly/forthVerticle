package com.ypc.forthVerticle.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private JDBCClient jdbcClient;

    UserServiceImpl(JDBCClient jdbcClient, Handler<AsyncResult<UserService>> readyHandler){
        this.jdbcClient = jdbcClient;

        jdbcClient.getConnection(ar -> {
            if (ar.succeeded()){
                SQLConnection connection = ar.result();
                connection.execute("select * from user",result -> {
                    connection.close();
                    if (result.succeeded()){
                    LOGGER.info(">>>> execute success <<<<");
                    readyHandler.handle(Future.succeededFuture(this));
                    } else {
                    LOGGER.error(">>>> execute failed <<<<");
                    readyHandler.handle(Future.failedFuture(result.cause()));
                   }
                });
            } else {
                readyHandler.handle(Future.failedFuture(ar.cause()));
                LOGGER.error(">>>> get database connection failed <<<<",ar.cause());
            }
        });

    }

    @Override
    public UserService selectUserById(Integer id,Handler<AsyncResult<JsonObject>> resultHandler) {
        LOGGER.info(">>>> user service impl selectUserById <<<<");
        jdbcClient.queryWithParams("select * from user where id = ?",new JsonArray().add(id),rst -> {
            if (rst.succeeded()){
                JsonObject jsonObject = rst.result().getRows().get(0);
                resultHandler.handle(Future.succeededFuture(jsonObject));
            } else {
                resultHandler.handle(Future.failedFuture(rst.cause()));
            }
        });

        return this;
    }

    @Override
    public UserService selectUserConditional(String address, Integer age, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        LOGGER.info(">>>> user service impl selectUserConditional <<<<");
        jdbcClient.queryWithParams("select * from user where address = ? and age = ?",new JsonArray().add(address).add(age),rst -> {
            if (rst.succeeded()){
                List<JsonObject> jsonObjectList = rst.result().getRows();
                resultHandler.handle(Future.succeededFuture(jsonObjectList));
            } else {
               resultHandler.handle(Future.failedFuture(rst.cause()));
            }
      });

      return this;
    }
}
