package org.jamie.springboot;

import org.springframework.context.ApplicationContext;
import org.jamie.springboot.webserver.WebServer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Map;

/**
 * @author jamie
 * @version 1.0.0
 * @description 自定义SpringApplication类
 * @date 2023/01/01 00:32
 */
public class JamieSpringApplication {

    public static ApplicationContext run(Class clazz) {
        return run(clazz, null);
    }

    public static ApplicationContext run(Class clazz, String[] args) {
        // 创建Spring容器
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(clazz);
        applicationContext.refresh();
        // 启动Tomcat/Jetty
        WebServer webServer = getWebServer(applicationContext);
        webServer.start();
        return applicationContext;
    }

    private static WebServer getWebServer(WebApplicationContext applicationContext) {
        // 判断返回哪种WebServer
        Map<String, WebServer> webServerMap = applicationContext.getBeansOfType(WebServer.class);
        if (webServerMap.size() == 0) {
            throw new NullPointerException();
        }
        if (webServerMap.size() > 1) {
            throw new IllegalStateException();
        }

        return webServerMap.values().stream().findFirst().get();
    }
}
