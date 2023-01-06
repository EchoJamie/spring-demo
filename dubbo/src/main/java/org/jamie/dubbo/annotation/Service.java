package org.jamie.dubbo.annotation;

import java.lang.annotation.*;

/**
 * @author jamie
 * @version 1.0.0
 * @description 服务实现类注解
 * @date 2023/01/03 13:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Service {
    String value();
    String version();
}
