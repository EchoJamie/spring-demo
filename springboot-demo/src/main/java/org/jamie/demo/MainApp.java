package org.jamie.demo;

import org.jamie.springboot.JamieSpringApplication;
import org.jamie.springboot.annotation.JamieSpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author jamie
 * @version 1.0.0
 * @description 主应用
 * @date 2023/01/01 00:32
 */
@JamieSpringBootApplication
public class MainApp {
    public static void main(String[] args) {
        ApplicationContext run = JamieSpringApplication.run(MainApp.class);
        Object userController = run.getBean("userController");
        System.out.println(userController);
    }
}
