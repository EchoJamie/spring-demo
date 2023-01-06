package org.jamie.spring.bean.exception;

/**
 * @author jamie
 * @version 1.0.0
 * @description Bean未找到 异常
 * @date 2022/12/30 00:04
 */
public class BeanNotFoundException extends BeanException{


    public BeanNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }

    public BeanNotFoundException(String message) {
        super(message);
    }

    public BeanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
