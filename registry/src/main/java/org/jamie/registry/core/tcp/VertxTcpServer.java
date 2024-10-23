package org.jamie.registry.core.tcp;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import org.jamie.registry.core.Server;

/**
 * tcp 服务端
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/22 19:31
 */
public class VertxTcpServer implements Server {

    private final Handler<NetSocket> handler;

    public VertxTcpServer(Handler<NetSocket> handler) {
        this.handler = handler;
    }

    /**
     *
     */
    @Override
    public void start(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer netServer = vertx.createNetServer();

        // 绑定连接处理器
        netServer.connectHandler(this.handler);

        // 启动监听服务
        netServer.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("VertxTcpServer started successfully");
            } else {
                System.out.println("Failed to start VertxTcpServer");
            }
        });
    }
}
