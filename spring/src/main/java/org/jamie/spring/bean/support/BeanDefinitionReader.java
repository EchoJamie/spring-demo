package org.jamie.spring.bean.support;

import org.jamie.spring.exception.BeanException;
import org.jamie.spring.resource.Resource;
import org.jamie.spring.resource.ResourceLoader;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 05:21
 */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeanException;

    void loadBeanDefinitions(String location) throws BeanException;

    void loadBeanDefinitions(String[] locations) throws BeanException;
}
