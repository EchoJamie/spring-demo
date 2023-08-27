package org.jamie.demo.util;

import org.springframework.beans.BeansException;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description TODO
 * @date 2023/4/17 16:15
 */
@Component
public class StaticUtil implements EnvironmentAware, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static ConfigurableEnvironment environment;
    public static RefreshScope refreshScope;
    public static ContextRefresher CONTEXT_REFRESHER;

    public StaticUtil(RefreshScope refreshScope, ContextRefresher contextRefresher) {
        this.refreshScope = refreshScope;
        CONTEXT_REFRESHER = contextRefresher;
    }


    public static void refresh() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("demo.test", "dsadwq41561");
        environment.getPropertySources().addLast(new MapPropertySource("dynamic", map));
        new Thread(() -> CONTEXT_REFRESHER.refresh()).start();
    }

    //public static void postRefresh() {
    //    String s = applicationContext.getBean(RestTemplate.class).postForObject("http://127.0.0.1:8888/actuator/refresh", null, String.class);
    //    System.out.println(s);
    //}


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        StaticUtil.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        StaticUtil.environment = (ConfigurableEnvironment) environment;
    }
}
