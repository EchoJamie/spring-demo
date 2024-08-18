package org.jamie.spring.bean;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 06:40
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);
}
