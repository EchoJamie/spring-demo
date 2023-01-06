package org.jamie.springboot.config;

import org.jamie.springboot.annotation.JamieConditionalOnClass;
import org.jamie.springboot.webserver.condition.JettyCondition;
import org.jamie.springboot.webserver.condition.TomcatCondition;
import org.jamie.springboot.webserver.impl.JettyWebServer;
import org.jamie.springboot.webserver.impl.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author jamie
 * @version 1.0.0
 * @description Web服务器 自动配置类
 * @date 2023/01/01 01:18
 */
@Configuration
public class WebServerAutoConfiguration {

    @Bean
//    @Conditional(TomcatCondition.class)
    @JamieConditionalOnClass("org.apache.catalina.startup.Tomcat")
    public TomcatWebServer tomcatWebServer(WebApplicationContext applicationContext) {
        return new TomcatWebServer(applicationContext);
    }

    @Bean
//    @Conditional(JettyCondition.class)
    @JamieConditionalOnClass("org.eclipse.jetty.server.Server")
    public JettyWebServer jettyWebServer() {
        return new JettyWebServer();
    }
}
