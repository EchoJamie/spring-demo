package org.jamie.spring.bean;

import lombok.Getter;
import lombok.Setter;
import org.jamie.spring.env.PropertyValues;

import static org.jamie.spring.bean.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
 * @author jamie
 * @version 1.0.0
 * @description Bean定义 原型
 * @date 2022/12/29 23:50
 */
public class BeanDefinition {

    @Setter
    @Getter
    private Class clazz;
    @Setter
    @Getter
    private String scope;

    @Setter
    @Getter
    private PropertyValues propertyValues;

    @Setter
    @Getter
    private String initMethodName;

    @Setter
    @Getter
    private String destroyMethodName;

    @Setter
    @Getter
    private boolean lazyInit;

    public BeanDefinition(Class clazz) {
        this(clazz, null);
    }

    public BeanDefinition(Class clazz, PropertyValues propertyValues) {
        this.clazz = clazz;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }
}
