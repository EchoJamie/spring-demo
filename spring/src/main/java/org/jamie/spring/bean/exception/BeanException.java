package org.jamie.spring.bean.exception;

/**
 * @author jamie
 * @version 1.0.0
 * @description Bean异常类
 * @date 2022/12/30 14:58
 */
public class BeanException extends RuntimeException{

    public BeanException() {
    }

    public BeanException(String message) {
        super(message);
    }

    public BeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanException(Throwable cause) {
        super(cause);
    }

    public BeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
