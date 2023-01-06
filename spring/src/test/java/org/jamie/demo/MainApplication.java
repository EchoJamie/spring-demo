package org.jamie.demo;

import org.jamie.demo.config.AppConfig;
import org.jamie.demo.processor.JamieBeanPostProcessor;
import org.jamie.demo.service.UserService;
import org.jamie.spring.context.impl.JamieApplicationContext;

public class MainApplication {

    public static void main(String[] args) {
        JamieApplicationContext applicationContext = new JamieApplicationContext(AppConfig.class);
        UserService bean1 = (UserService) applicationContext.getBean("userService");
        UserService bean2 = (UserService) applicationContext.getBean("userService");
        UserService bean3 = (UserService) applicationContext.getBean("userService");
        bean1.test();
        bean2.test();
        bean3.test();

        JamieBeanPostProcessor jamieBeanPostProcessor = (JamieBeanPostProcessor) applicationContext.getBean("jamieBeanPostProcessor");

        System.out.println(jamieBeanPostProcessor);
//        System.out.println(applicationContext.getBean("aaa"));
    }
}