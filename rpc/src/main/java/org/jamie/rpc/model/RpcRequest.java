package org.jamie.rpc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 请求信息
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 16:36
 */
@Getter
@Setter
@ToString
public class RpcRequest implements Serializable {

    private String serviceVersion;

    private String serviceName;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;
}
