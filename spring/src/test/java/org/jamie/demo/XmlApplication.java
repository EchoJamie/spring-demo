package org.jamie.demo;

import org.jamie.demo.processor.JamieBeanPostProcessor;
import org.jamie.demo.service.UserService;
import org.jamie.spring.context.impl.ClassPathXmlApplicationContext;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 08:10
 */
public class XmlApplication {

    public static final String defaultSpringXml = "classpath:spring.xml";

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(defaultSpringXml);
        UserService bean1 = (UserService) applicationContext.getBean("userService");
        UserService bean2 = (UserService) applicationContext.getBean("userService");
        UserService bean3 = (UserService) applicationContext.getBean("userService");
        bean1.test();
        bean2.test();
        bean3.test();

        JamieBeanPostProcessor jamieBeanPostProcessor = (JamieBeanPostProcessor) applicationContext.getBean("jamieBeanPostProcessor");

        System.out.println(jamieBeanPostProcessor);
    }
}
