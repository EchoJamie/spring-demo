package org.jamie.springboot.annotation;

import org.jamie.springboot.webserver.condition.ClassCondition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author jamie
 * @version 1.0.0
 * @description 存在类依赖 判断注解
 * @date 2023/01/01 01:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(ClassCondition.class)
public @interface JamieConditionalOnClass {
    String value();
}
