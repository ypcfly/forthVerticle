package com.ypc.forthVerticle.httpServer;

import com.google.gson.Gson;
import com.ypc.forthVerticle.entity.User;
import com.ypc.forthVerticle.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

public class HttpServerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    private static final String ADDRESS = "data.service";

    private UserService userService;

    private final FreeMarkerTemplateEngine templateEngine = FreeMarkerTemplateEngine.create();

    @Override
    public void start(Future<Void> startFuture) {

        userService = UserService.createProxy(vertx,ADDRESS);

        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        // 设置请求访问地址
        router.get("/index").handler(this::indexHandler);
        router.get("/user/:id").handler(this::queryByIdHandler);
        router.get("/user/:age/:address").handler(this::queryByCondition);

        httpServer.requestHandler(router::accept).listen(9090,ar -> {
            if (ar.succeeded()){
                LOGGER.error(">>>> http server success <<<<");
                startFuture.complete();
            } else {
                LOGGER.error(">>>> http server failed <<<<");
                startFuture.fail(ar.cause());
            }

        });

    }

    private void queryByCondition(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String param = request.getParam("age");
        String address = request.getParam("address");
        Integer age = Integer.parseInt(param);
        LOGGER.info(">>>> select user info by age and address <<<<");

        userService.selectUserConditional(address,age, reply -> {
            if (reply.succeeded()){
                List<JsonObject> jsonObjectList = reply.result();
                Gson gson = new Gson();
                List<User> list = new ArrayList<>();
                User user = null;
                for (JsonObject jsonObject:jsonObjectList){
                    user = gson.fromJson(jsonObject.toString(), User.class);
                    list.add(user);
                }
                routingContext.put("list",list);
                templateEngine.render(routingContext,"templates","/user/user.ftl",ar -> {
                    if (ar.succeeded()){
                        routingContext.response().putHeader("content-type","text/html");
                        routingContext.response().end(ar.result());
                    } else {
                        LOGGER.error(ar.cause().getMessage(),ar.cause());
                        routingContext.fail(ar.cause());
                    }
                });

            } else {
                routingContext.fail(reply.cause());
            }
        });

    }

  /**
     *
     * @param context
     */
    private void queryByIdHandler(RoutingContext context) {
        String param = context.request().getParam("id");
        Integer id = Integer.parseInt(param);
        LOGGER.info(">>>> select user info by userId <<<<");
        userService.selectUserById(id, reply ->{
            if (reply.succeeded()){
                JsonObject jsonObject = reply.result();
                Gson gson = new Gson();
                User user = gson.fromJson(jsonObject.toString(), User.class);
                List<User> list = new ArrayList<>();
                list.add(user);
//                String js = JSONObject.toJSONString(jsonObject);
//                List<User> list = JSONObject.parseArray(js,User.class);
                context.put("list",list);
                templateEngine.render(context,"templates","/user/user.ftl",ar -> {
                    if (ar.succeeded()){
                        context.response().putHeader("content-type","text/html");
                        context.response().end(ar.result());
                    } else {
                        LOGGER.error(ar.cause().getMessage(),ar.cause());
                        context.fail(ar.cause());
                    }
                });

            } else {
                context.fail(reply.cause());
            }
        });

    }


    /**
     *
     * @param routingContext
     */
    private void indexHandler(RoutingContext routingContext) {
          templateEngine.render(routingContext,"templates","/index.ftl",ar -> {
              if (ar.succeeded()){
                  routingContext.response().putHeader("content-type","text/html");
                  routingContext.response().end(ar.result());
              } else {
                  routingContext.fail(ar.cause());
              }
          });

    }

}
