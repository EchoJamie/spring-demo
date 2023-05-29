package org.jamie.security.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description spring security 配置
 * @date 2023/5/2 19:50
 */
// @EnableWebSecurity
// @Configuration
public class SecurityOldConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
