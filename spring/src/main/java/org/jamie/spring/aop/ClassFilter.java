package org.jamie.spring.aop;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/26 01:17
 */
public interface ClassFilter {
    boolean matches(Class<?> clazz);
}
