package org.jamie.spring.annotation;

import java.lang.annotation.*;

/**
 * @author jamie
 * @version 1.0.0
 * @description TODO
 * @date 2022/12/29 23:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
public @interface Component {

    /**
     * BeanName
     * @return BeanName
     */
    String value();

    String scope() default "singleton";
}
