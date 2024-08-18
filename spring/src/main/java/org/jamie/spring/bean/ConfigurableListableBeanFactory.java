package org.jamie.spring.bean;

import org.jamie.spring.bean.config.ConfigurableBeanFactory;
import org.jamie.spring.bean.support.AutowireCapableBeanFactory;
import org.jamie.spring.exception.BeanException;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 04:55
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    /**
     * 根据名称查找BeanDefinition
     *
     * @param beanName
     * @return
     * @throws BeanException 如果找不到BeanDefintion
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeanException;

    /**
     * 提前实例化所有单例实例
     *
     * @throws BeanException
     */
    void preInstantiateSingletons() throws BeanException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
