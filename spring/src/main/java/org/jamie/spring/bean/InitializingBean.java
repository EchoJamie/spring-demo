package org.jamie.spring.bean;

/**
 * @author jamie
 * @version 1.0.0
 * @description Bean初始化 接口
 * @date 2022/12/30 00:43
 */
public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
