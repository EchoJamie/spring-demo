package org.jamie.spring.bean.support;

import org.jamie.spring.bean.BeanDefinition;
import org.jamie.spring.exception.BeanException;

/**
 * 实例化策略
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 01:34
 */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition) throws BeanException;
}
