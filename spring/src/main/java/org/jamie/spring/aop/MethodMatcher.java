package org.jamie.spring.aop;

import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/26 01:17
 */
public interface MethodMatcher {
    boolean matches(Method method, Class<?> targetClass);
}
