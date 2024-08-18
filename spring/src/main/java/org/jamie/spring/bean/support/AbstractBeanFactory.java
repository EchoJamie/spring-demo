package org.jamie.spring.bean.support;

import lombok.Getter;
import org.jamie.spring.bean.BeanDefinition;
import org.jamie.spring.bean.BeanPostProcessor;
import org.jamie.spring.bean.FactoryBean;
import org.jamie.spring.bean.config.ConfigurableBeanFactory;
import org.jamie.spring.bean.config.DefaultSingletonBeanRegistry;
import org.jamie.spring.core.ConversionService;
import org.jamie.spring.exception.BeanException;
import org.jamie.spring.util.StringValueResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:50
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    @Getter
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    @Getter
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    private ConversionService conversionService;

    @Override
    public Object getBean(String beanName) {
        Object bean = getSingleton(beanName);
        if (bean != null) {
            return getObjectForBeanInstance(bean, beanName);
        }

        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        bean = createBean(beanDefinition, beanName);
        return getObjectForBeanInstance(bean, beanName);
    }

    /**
     * 如果是FactoryBean，从FactoryBean#getObject中创建bean
     *
     * @param beanInstance
     * @param beanName
     * @return
     */
    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        Object object = beanInstance;
        if (beanInstance instanceof FactoryBean) {
            FactoryBean factoryBean = (FactoryBean) beanInstance;
            try {
                if (factoryBean.isSingleton()) {
                    //singleton作用域bean，从缓存中获取
                    object = this.factoryBeanObjectCache.get(beanName);
                    if (object == null) {
                        object = factoryBean.getObject();
                        this.factoryBeanObjectCache.put(beanName, object);
                    }
                } else {
                    object = factoryBean.getObject();
                }
            } catch (Exception ex) {
                throw new BeanException("FactoryBean threw exception on object[" + beanName + "] creation", ex);
            }
        }
        return object;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        getBeanPostProcessors().remove(beanPostProcessor);
        getBeanPostProcessors().add(beanPostProcessor);
    }

    /**
     * @param valueResolver
     */
    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        getEmbeddedValueResolvers().remove(valueResolver);
        getEmbeddedValueResolvers().add(valueResolver);
    }

    /**
     * @param value
     * @return
     */
    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver embeddedValueResolver : embeddedValueResolvers) {
            result = embeddedValueResolver.resolveStringValue(result);
        }
        return result;
    }

    /**
     * @param conversionService
     */
    @Override
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * @return
     */
    @Override
    public ConversionService getConversionService() {
        return conversionService;
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) {
        return null;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeanException;

    protected abstract Object createBean(BeanDefinition beanDefinition, String beanName) throws BeanException;
}
