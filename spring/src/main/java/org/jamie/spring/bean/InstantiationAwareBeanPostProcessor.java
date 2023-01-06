package org.jamie.spring.bean;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 实例化前
     * @param beanClass
     * @param beanName
     * @return
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);

    /**
     * 实例化后
     * @param bean
     * @param beanName
     * @return
     */
    boolean postProcessAfterInstantiation(Object bean, String beanName);

}