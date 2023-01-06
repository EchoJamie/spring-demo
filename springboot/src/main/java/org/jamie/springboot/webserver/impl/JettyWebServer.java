package org.jamie.springboot.webserver.impl;

import org.jamie.springboot.webserver.WebServer;

/**
 * @author jamie
 * @version 1.0.0
 * @description Jetty 服务器
 * @date 2023/01/01 01:09
 */
public class JettyWebServer implements WebServer {
    @Override
    public void start() {
        // Jetty启动方法
        System.out.println("启动Jetty");
    }
}
