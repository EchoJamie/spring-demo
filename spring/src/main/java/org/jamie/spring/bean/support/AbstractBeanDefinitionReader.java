package org.jamie.spring.bean.support;

import lombok.Setter;
import org.jamie.spring.exception.BeanException;
import org.jamie.spring.resource.DefaultResourceLoader;
import org.jamie.spring.resource.ResourceLoader;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 05:23
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    @Setter
    private ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this(beanDefinitionRegistry, new DefaultResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry, ResourceLoader resourceLoader) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.resourceLoader = resourceLoader;
    }

    /**
     * @return
     */
    @Override
    public BeanDefinitionRegistry getRegistry() {
        return beanDefinitionRegistry;
    }

    /**
     * @return
     */
    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    /**
     * @param locations
     * @throws BeanException
     */
    @Override
    public void loadBeanDefinitions(String[] locations) throws BeanException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }
}
