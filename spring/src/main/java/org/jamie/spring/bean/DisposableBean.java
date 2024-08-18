package org.jamie.spring.bean;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 05:04
 */
public interface DisposableBean {

    void destroy() throws Exception;
}
