package org.jamie.spring.bean;

import org.jamie.spring.exception.BeanException;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:41
 */
public interface BeanFactory {

    Object getBean(String beanName) throws BeanException;

    <T> T getBean(String beanName, Class<T> requiredType) throws BeanException;

    <T> T getBean(Class<T> requiredType) throws BeanException;

    boolean containsBean(String name);
}
