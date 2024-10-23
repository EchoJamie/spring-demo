package org.jamie.registry.core.http;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import org.jamie.registry.core.Server;

/**
 * http 服务端
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/22 19:28
 */
public class HttpServer implements Server {

    private final Handler<HttpServerRequest> handler;

    public HttpServer(Handler<HttpServerRequest> handler) {
        this.handler = handler;
    }


    /**
     * 启动
     */
    @Override
    public void start(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 HTTP 服务器
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        // 绑定处理器
        httpServer.requestHandler(handler);

        // 启动监听服务
        httpServer.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("HTTP 服务器启动成功");
            } else {
                System.out.println("HTTP 服务器启动失败");
            }
        });
    }
}
