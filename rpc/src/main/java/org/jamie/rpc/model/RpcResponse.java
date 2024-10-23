package org.jamie.rpc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 响应信息
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 16:36
 */
@Getter
@Setter
@ToString
public class RpcResponse implements Serializable {

    private Object result;

    private Class<?> resultClass;

    private String message;

    private Throwable throwable;
}
