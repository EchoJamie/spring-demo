package org.jamie.spring.bean.config;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:46
 */
public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);

    void addSingleton(String beanName, Object singletonObject);
}
