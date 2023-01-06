package org.jamie.demo.processor;

import org.jamie.demo.service.impl.UserServiceImpl;
import org.jamie.spring.annotation.Component;
import org.jamie.spring.bean.BeanPostProcessor;

import java.lang.reflect.Proxy;

/**
 * @author jamie
 * @version 1.0.0
 * @description 自定义Bean后置处理器
 * @date 2022/12/30 15:08
 */
@Component("jamieBeanPostProcessor")
public class JamieBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println(beanName + "初始化前");
        if ("userService".equals(beanName)) {
            ((UserServiceImpl) bean).setInitStr("Jamie 666~~~");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println(beanName + "初始化后");
        if ("userService".equals(beanName)) {
            Object proxyInstance = Proxy.newProxyInstance(JamieBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
                System.out.println("代理前逻辑");
                Object invoke = method.invoke(bean, args);
                System.out.println("代理后逻辑");
                return invoke;
            });
            return proxyInstance;
        }
        return bean;
    }
}
