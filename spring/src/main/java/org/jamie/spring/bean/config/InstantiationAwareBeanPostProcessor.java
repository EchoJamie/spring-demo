package org.jamie.spring.bean.config;

import org.jamie.spring.bean.BeanPostProcessor;
import org.jamie.spring.env.PropertyValues;
import org.jamie.spring.exception.BeanException;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 06:41
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 在bean实例化之前执行
     *
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeanException
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeanException;

    /**
     * bean实例化之后，设置属性之前执行
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeanException
     */
    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeanException;

    /**
     * bean实例化之后，设置属性之前执行
     *
     * @param pvs
     * @param bean
     * @param beanName
     * @return
     * @throws BeanException
     */
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName)
            throws BeanException;

    /**
     * 提前暴露bean
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeanException
     */
    default Object getEarlyBeanReference(Object bean, String beanName) throws BeanException {
        return bean;
    }
}
