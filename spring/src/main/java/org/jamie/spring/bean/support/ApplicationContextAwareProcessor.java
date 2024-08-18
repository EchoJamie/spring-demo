package org.jamie.spring.bean.support;

import org.jamie.spring.bean.BeanPostProcessor;
import org.jamie.spring.context.ApplicationContext;
import org.jamie.spring.context.ApplicationContextAware;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 09:22
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {
    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化前
     *
     * @param bean
     * @param beanName
     * @return
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    /**
     * 初始化后
     *
     * @param bean
     * @param beanName
     * @return
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
