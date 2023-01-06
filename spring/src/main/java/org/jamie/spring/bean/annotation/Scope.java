package org.jamie.spring.bean.annotation;

import org.jamie.spring.bean.constant.ScopeConstant;

import java.lang.annotation.*;

/**
 * @author jamie
 * @version 1.0.0
 * @description 域注解
 * @date 2022/12/29 23:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Scope {

    ScopeConstant value() default ScopeConstant.SINGLETON;
}
