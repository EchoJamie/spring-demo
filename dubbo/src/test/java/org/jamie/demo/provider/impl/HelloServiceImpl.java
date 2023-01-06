package org.jamie.demo.provider.impl;

import org.jamie.demo.provider.api.HelloService;

/**
 * @author jamie
 * @version 1.0.0
 * @description Hello服务实现类
 * @date 2023/01/02 12:04
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String age(Integer age) {
        return "my age is " + age;
    }

    @Override
    public String hello(String username) {
        return "hello 8889" + username;
    }

    @Override
    public String world() {
        return "world";
    }
}
