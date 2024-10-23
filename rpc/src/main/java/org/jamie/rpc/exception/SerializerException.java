package org.jamie.rpc.exception;

import java.io.IOException;

/**
 * 序列化异常
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 13:08
 */
public class SerializerException extends IOException {

    public SerializerException() {
        super();
    }

    public SerializerException(String message) {
        super(message);
    }

    public SerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializerException(Throwable cause) {
        super(cause);
    }
}
