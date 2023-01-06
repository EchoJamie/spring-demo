package org.jamie.dubbo.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author jamie
 * @version 1.0.0
 * @description TODO
 * @date 2023/01/02 12:24
 */
@Getter
@Setter
@AllArgsConstructor
public class Invocation implements Serializable {

    private String interfaceName;

    private String methodName;

    private Class[] paramTypes;

    private Object[] params;

    private String version;
}
