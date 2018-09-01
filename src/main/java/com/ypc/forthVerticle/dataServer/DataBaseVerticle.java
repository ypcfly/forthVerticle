package com.ypc.forthVerticle.dataServer;

import com.ypc.forthVerticle.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ProxyHelper;

public class DataBaseVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseVerticle.class);

    private static final String ADDRESS = "data.service";

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/springboot?useSSL=false&characterEncoding=utf8";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "123";
    private static final int MAX_POOL_SIZE = 30;

    private JDBCClient jdbcClient;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        LOGGER.info(">>>> database verticle start <<<<");

//      JsonObject config = new JsonObject();
//      config.put("url",URL)
//      .put("driver_class",DRIVER_CLASS)
//      .put("user_name",USER_NAME)
//      .put("password",PASSWORD)
//      .put("max_pool_size",MAX_POOL_SIZE);

        JsonObject config = new JsonObject().put("url",URL).put("driver_class",DRIVER_CLASS)
          .put("user",USER_NAME).put("password",PASSWORD).put("max_pool_size",MAX_POOL_SIZE);

        jdbcClient = JDBCClient.createShared(vertx,config);
        UserService.create(jdbcClient,ar ->{
            if (ar.succeeded()){
              LOGGER.info(">>>> register service success <<<<");
              ProxyHelper.registerService(UserService.class,vertx,ar.result(),ADDRESS);
              startFuture.complete();
            } else {
              LOGGER.error(">>>> register service failed <<<<");
              startFuture.fail(ar.cause());
            }
          });

    }

}
