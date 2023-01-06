package org.jamie.springboot.annotation;

import org.jamie.springboot.config.AutoConfigurationImportSelector;
import org.jamie.springboot.config.WebServerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author jamie
 * @version 1.0.0
 * @description SpringBoot应用注解
 * @date 2023/01/01 00:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@ComponentScan
@Import(WebServerAutoConfiguration.class)
public @interface JamieSpringBootApplication {
}
