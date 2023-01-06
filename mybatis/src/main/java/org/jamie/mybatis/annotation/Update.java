package org.jamie.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author jamie
 * @version 1.0.0
 * @description Update 注解
 * @date 2023/01/02 05:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Update {

    String value();
}
