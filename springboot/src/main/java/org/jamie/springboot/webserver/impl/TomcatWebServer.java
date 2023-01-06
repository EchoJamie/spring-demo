package org.jamie.springboot.webserver.impl;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.jamie.springboot.webserver.WebServer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author jamie
 * @version 1.0.0
 * @description Tomcat 服务器
 * @date 2023/01/01 01:09
 */
public class TomcatWebServer implements WebServer {

    private WebApplicationContext applicationContext;
    public static final String LOCALHOST = "localhost";
    public static final String SERVLET_NAME = "dispatcher";

    public TomcatWebServer(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void start() {
        // Tomcat启动方法
        System.out.println("启动Tomcat");
        Tomcat tomcat = new Tomcat();

        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(8080);

        Engine engine = new StandardEngine();

        engine.setDefaultHost(LOCALHOST);

        Host host = new StandardHost();
        host.setName(LOCALHOST);

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);

        tomcat.addServlet(contextPath, SERVLET_NAME, new DispatcherServlet(applicationContext));
        context.addServletMappingDecoded("/*", SERVLET_NAME);

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
