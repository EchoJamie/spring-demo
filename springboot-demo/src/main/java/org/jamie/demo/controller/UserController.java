package org.jamie.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jamie
 * @version 1.0.0
 * @description User控制器
 * @date 2023/01/01 01:04
 */
@RestController
public class UserController {

    @RequestMapping("/user")
    public String user() {
        return "user";
    }
}
