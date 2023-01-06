package org.jamie.spring.annotation;

import java.lang.annotation.*;

/**
 * @author jamie
 * @version 1.0.0
 * @description 组件扫描注解
 * @date 2022/12/29 23:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {

    String[] value();
}
