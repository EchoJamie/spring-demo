package org.jamie.spring.annotation;

import java.lang.annotation.*;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 配置类注解
 * @date 2022/12/29 22:35
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Configuration {
    String value() default "";
}
