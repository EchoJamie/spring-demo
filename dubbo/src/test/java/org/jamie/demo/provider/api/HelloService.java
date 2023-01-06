package org.jamie.demo.provider.api;

/**
 * @author jamie
 * @version 1.0.0
 * @description Hello服务接口
 * @date 2023/01/02 12:03
 */
public interface HelloService {

    String age(Integer age);

    String hello(String username);

    String world();
}
