package org.jamie.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description TODO
 * @date 2023/02/14 21:59
 */
@Component
@RefreshScope
@ConfigurationProperties("demo")
public class DemoProperties {

    private String test;

    public void setTest(String test) {
        this.test = test;
    }

    public String getTest() {
        return test;
    }
}
