package org.jamie.spring.bean.config;

import org.jamie.spring.bean.BeanPostProcessor;
import org.jamie.spring.bean.HierarchicalBeanFactory;
import org.jamie.spring.core.ConversionService;
import org.jamie.spring.util.StringValueResolver;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 04:50
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    /**
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 销毁单例bean
     */
    void destroySingletons();

    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    String resolveEmbeddedValue(String value);

    void setConversionService(ConversionService conversionService);

    ConversionService getConversionService();
}
