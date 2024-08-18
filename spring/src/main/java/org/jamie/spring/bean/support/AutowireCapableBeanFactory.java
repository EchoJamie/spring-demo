package org.jamie.spring.bean.support;

import org.jamie.spring.bean.BeanFactory;
import org.jamie.spring.exception.BeanException;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 02:27
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 执行BeanPostProcessors的postProcessBeforeInitialization方法
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeanException
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeanException;

    /**
     * 执行BeanPostProcessors的postProcessAfterInitialization方法
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeanException
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeanException;
}
