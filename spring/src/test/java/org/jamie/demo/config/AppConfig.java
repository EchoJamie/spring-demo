package org.jamie.demo.config;

import org.jamie.spring.annotation.ComponentScan;
import org.jamie.spring.annotation.Configuration;

/**
 * @author jamie
 * @version 1.0.0
 * @description 应用配置类
 * @date 2022/12/29 22:56
 */
@Configuration
@ComponentScan({"org.jamie.demo.service", "org.jamie.demo.processor"})
public class AppConfig {
}
