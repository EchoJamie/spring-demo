package org.jamie.spring.bean.annotation;

import java.lang.annotation.*;

import static org.jamie.spring.bean.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

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

    String value() default SCOPE_SINGLETON;
}
