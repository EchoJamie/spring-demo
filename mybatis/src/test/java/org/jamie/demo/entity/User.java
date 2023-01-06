package org.jamie.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jamie
 * @version 1.0.0
 * @description User实体类
 * @date 2023/01/02 05:17
 */
@Getter
@Setter
@ToString
public class User {

    private String id;

    private String name;

    private Integer age;
}
