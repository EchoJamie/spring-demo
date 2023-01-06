package org.jamie.spring.bean;

import org.jamie.spring.bean.constant.ScopeConstant;

/**
 * @author jamie
 * @version 1.0.0
 * @description Bean定义 原型
 * @date 2022/12/29 23:50
 */
public class BeanDefinition {

    private Class clazz;
    private ScopeConstant scope;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public ScopeConstant getScope() {
        return scope;
    }

    public void setScope(ScopeConstant scope) {
        this.scope = scope;
    }
}
