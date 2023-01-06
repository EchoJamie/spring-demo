package org.jamie.spring.context;

/**
 * @author jamie
 * @version 1.0.0
 * @description 应用上下文
 * @date 2022/12/29 22:38
 */
public interface ApplicationContext {


    Object getBean(String beanName);

    Object getBean(Class clazz);
}
