package org.jamie.spring.bean;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/26 01:02
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    boolean isSingleton();
}
