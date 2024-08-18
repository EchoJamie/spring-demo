package org.jamie.spring.bean.support;

import org.jamie.spring.bean.BeanDefinition;
import org.jamie.spring.exception.BeanException;

/**
 * BeanDefinition注册表接口
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:42
 */
public interface BeanDefinitionRegistry {


    /**
     * 向注册表中注BeanDefinition
     *
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 根据名称查找BeanDefinition
     *
     * @param beanName
     * @return
     * @throws BeanException 如果找不到BeanDefintion
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeanException;

    /**
     * 是否包含指定名称的BeanDefinition
     *
     * @param beanName
     * @return
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 返回定义的所有bean的名称
     *
     * @return
     */
    String[] getBeanDefinitionNames();

}
