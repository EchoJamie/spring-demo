package org.jamie.spring.bean.annotation;

import org.jamie.spring.bean.BeanFactory;
import org.jamie.spring.bean.BeanFactoryAware;
import org.jamie.spring.bean.ConfigurableListableBeanFactory;
import org.jamie.spring.bean.config.InstantiationAwareBeanPostProcessor;
import org.jamie.spring.core.ConversionService;
import org.jamie.spring.env.PropertyValues;
import org.jamie.spring.exception.BeanException;
import org.jamie.spring.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 06:43
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeanException {
        // 处理@Value注解
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Value valueAnnotation = field.getDeclaredAnnotation(Value.class);
            if (Objects.nonNull(valueAnnotation)) {
                Object value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue((String) value);
                Class<?> source = value.getClass();
                Class<?> target = (Class<?>) field.getGenericType();
                ConversionService conversionService = beanFactory.getConversionService();
                if (Objects.nonNull(conversionService)) {
                    if (conversionService.canConvert(source, target)) {
                        value = conversionService.convert(value, target);
                    }
                }
                ReflectUtils.setField(bean, field.getName(), value);
            }
        }

        // 处理@Autowired注解
        for (Field field : fields) {
            Autowired autowiredAnnotation = field.getDeclaredAnnotation(Autowired.class);
            if (Objects.nonNull(autowiredAnnotation)) {
                Class<?> fieldGenericType = (Class<?>) field.getGenericType();
                Qualifier qualifierAnnotation = field.getDeclaredAnnotation(Qualifier.class);
                Object dependentBean = null;
                if (Objects.nonNull(qualifierAnnotation)) {
                    String name = qualifierAnnotation.value();
                    dependentBean = beanFactory.getBean(name, fieldGenericType);
                } else {
                    dependentBean = beanFactory.getBean(fieldGenericType);
                }
                ReflectUtils.setField(bean, field.getName(), dependentBean);
            }
        }
        return pvs;
    }

    /**
     * 在bean实例化之前执行
     *
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeanException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeanException {
        return null;
    }

    /**
     * bean实例化之后，设置属性之前执行
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeanException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeanException {
        return true;
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
        return null;
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
        return null;
    }
}
