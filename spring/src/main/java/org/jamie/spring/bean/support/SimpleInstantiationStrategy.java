package org.jamie.spring.bean.support;

import org.jamie.spring.bean.BeanDefinition;
import org.jamie.spring.exception.BeanException;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 01:41
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {
    /**
     * @param beanDefinition
     * @return
     * @throws BeanException
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeanException {
        Class clazz = beanDefinition.getClazz();
        try {
            Object object = clazz.newInstance();
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
