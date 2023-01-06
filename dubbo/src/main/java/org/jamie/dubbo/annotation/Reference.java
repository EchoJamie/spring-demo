package org.jamie.dubbo.annotation;

import java.lang.annotation.*;

/**
 * @author jamie
 * @version 1.0.0
 * @description TODO
 * @date 2023/01/02 12:55
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Reference {

    String version();
}
