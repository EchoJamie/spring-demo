package org.jamie.security.demo.exception;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 项目顶层 运行时异常
 * @date 2023/5/28 16:31
 */
public class JamieException extends RuntimeException {

    public JamieException(String message, Throwable cause) {
        super(message, cause);
    }

    public JamieException(Throwable cause) {
        super(cause);
    }
}
