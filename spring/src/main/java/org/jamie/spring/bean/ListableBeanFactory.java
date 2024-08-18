package org.jamie.spring.bean;

import org.jamie.spring.exception.BeanException;

import java.util.Map;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:59
 */
public interface ListableBeanFactory extends BeanFactory {

    /**
     * 返回指定类型的所有实例
     *
     * @param type
     * @param <T>
     * @return
     * @throws BeanException
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeanException;

    /**
     * 返回定义的所有bean的名称
     *
     * @return
     */
    String[] getBeanDefinitionNames();
}
