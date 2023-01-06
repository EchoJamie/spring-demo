package org.jamie.demo.provider;

import org.jamie.demo.provider.api.HelloService;
import org.jamie.demo.provider.impl.HelloServiceImpl;
import org.jamie.dubbo.protocol.http.HttpServer;
import org.jamie.dubbo.register.LocalRegister;
import org.jamie.dubbo.register.RemoteServiceRegister;
import org.jamie.dubbo.register.URL;

import java.net.NetworkInterface;

/**
 * @author jamie
 * @version 1.0.0
 * @description 服务提供者
 * @date 2023/01/02 10:32
 */
public class Provider {

    public static void main(String[] args) {
        String version1 = "1.0";
        String version2 = "2.0";


        LocalRegister.register(HelloService.class.getName(), version1, HelloServiceImpl.class);

        URL url = new URL("127.0.0.1", 8889);
        RemoteServiceRegister.register(HelloService.class.getName(), version1, url);
        // Tomcat Jetty Netty SocketServer
        // 启动服务 接受请求
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getIp(), url.getPort());
    }
}
