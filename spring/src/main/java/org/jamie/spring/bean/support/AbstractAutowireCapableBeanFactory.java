package org.jamie.spring.bean.support;

import org.jamie.spring.bean.*;
import org.jamie.spring.bean.config.BeanReference;
import org.jamie.spring.bean.config.InstantiationAwareBeanPostProcessor;
import org.jamie.spring.env.PropertyValue;
import org.jamie.spring.env.PropertyValues;
import org.jamie.spring.exception.BeanException;
import org.jamie.spring.util.ReflectUtils;
import org.jamie.spring.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 01:11
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    @Override
    protected Object createBean(BeanDefinition beanDefinition, String beanName) throws BeanException {
        return doCreateBean(beanDefinition, beanName);
    }

    protected Object doCreateBean(BeanDefinition beanDefinition, String beanName) throws BeanException {
        Object bean = createBeanInstance(beanDefinition, beanName);
        //在设置bean属性之前，允许BeanPostProcessor修改属性值
        applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
        // 注入属性
        applyPropertyValues(beanName, bean, beanDefinition);
        // 实例化
        Object warppedBean = initializeBean(beanName, bean, beanDefinition);

        registerDisposableBeanIfNecessary(beanName, warppedBean, beanDefinition);

        if (beanDefinition.isSingleton()) {
            addSingleton(beanName, warppedBean);
        }
        return warppedBean;
    }

    private void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (bean instanceof DisposableBean || StringUtils.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName,  new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }

    protected abstract Object createBeanInstance(BeanDefinition beanDefinition, String beanName) throws BeanException;

    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (Objects.isNull(beanDefinition.getPropertyValues())) {
            return;
        }
        beanDefinition.getPropertyValues().forEach(pv -> {
            for (Field field : fields) {
                if (field.getName().equals(pv.getName())) {
                    Object value = pv.getValue();
                    // 判断 BeanReference
                    if (value instanceof BeanReference) {
                        BeanReference beanReference = (BeanReference) value;
                        value = getBean(beanReference.getBeanName());
                    }
                    ReflectUtils.setField(bean, field.getName(), value);
                    break;
                }
            }
        });
    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // Aware回调  BeanFactoryAware接口
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }

        // Aware回调  BeanNameAware接口
        if (bean instanceof BeanNameAware) {
            ((BeanNameAware) bean).setBeanName(beanName);
        }

        // BeanPostProcessor 初始化前
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Throwable ex) {
            throw new BeanException("Invocation of init method of bean[" + beanName + "] failed", ex);
        }

        // BeanPostProcessor 初始化后
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {
        // Aware回调  初始化
        if (wrappedBean instanceof InitializingBean) {
            try {
                ((InitializingBean) wrappedBean).afterPropertiesSet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String initMethodName = beanDefinition.getInitMethodName();
        if (Objects.nonNull(initMethodName)) {
            ReflectUtils.invokePublic(wrappedBean, beanDefinition.getInitMethodName(), (e) -> {
                throw new BeanException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
            });
        }
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) throws BeanException {
        Object result = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if (Objects.isNull(current)) {
                return result;
            }
            result = current;
        }
        return result;
    }

    protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if (Objects.nonNull(pvs)) {
                    for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }
            }
        }
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) throws BeanException {
        Object result = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if (Objects.isNull(current)) {
                return result;
            }
            result = current;
        }
        return result;
    }
}
