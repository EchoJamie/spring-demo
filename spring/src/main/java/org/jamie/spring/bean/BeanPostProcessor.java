package org.jamie.spring.bean;

/**
 * @author jamie
 * @version 1.0.0
 * @description Bean后置处理器 接口
 * @date 2022/12/30 00:49
 */
public interface BeanPostProcessor {

    /**
     * 初始化前
     *
     * @param bean
     * @param beanName
     * @return
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 初始化后
     *
     * @param bean
     * @param beanName
     * @return
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
