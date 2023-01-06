package org.jamie.dubbo.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author jamie
 * @version 1.0.0
 * @description IP + Port
 * @date 2023/01/03 15:42
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class URL implements Serializable {

    private String ip;

    private Integer port;
}
