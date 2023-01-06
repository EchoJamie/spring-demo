package org.jamie.demo.consumer;

import org.jamie.demo.provider.api.HelloService;
import org.jamie.dubbo.register.ProxyFactory;

/**
 * @author jamie
 * @version 1.0.0
 * @description 服务消费者
 * @date 2023/01/02 10:32
 */
public class Consumer {

    public static void main(String[] args) {

        HelloService helloService = ProxyFactory.getProxy(HelloService.class);

        System.out.println(helloService.hello("Jamie"));
        System.out.println(helloService.world());
        System.out.println(helloService.age(25));
    }
}
