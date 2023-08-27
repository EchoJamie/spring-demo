package org.jamie.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 主程序
 * @date 2023/02/14 21:54
 */
@SpringBootApplication
public class DemoApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApp.class, args);
        System.out.println("hello");
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

}
