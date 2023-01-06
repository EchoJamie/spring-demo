package org.jamie.spring.bean;

/**
 * @author jamie
 * @version 1.0.0
 * @description BeanName 回调方法
 * @date 2022/12/30 00:36
 */
public interface BeanNameAware extends Aware{

    /**
     * 获取BeanName
     * @param name
     */
    void setBeanName(String name);
}
