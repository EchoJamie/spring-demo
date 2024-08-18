package org.jamie.spring.bean.config;

import org.jamie.spring.bean.ConfigurableListableBeanFactory;
import org.jamie.spring.exception.BeanException;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 08:57
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在所有BeanDefintion加载完成后，但在bean实例化之前，提供修改BeanDefinition属性值的机制
     *
     * @param beanFactory
     * @throws BeanException
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeanException;


}
