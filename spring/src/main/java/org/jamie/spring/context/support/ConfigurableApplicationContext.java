package org.jamie.spring.context.support;

import org.jamie.spring.context.ApplicationContext;
import org.jamie.spring.exception.BeanException;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 09:15
 */
public interface ConfigurableApplicationContext extends ApplicationContext {


    /**
     * 刷新容器
     *
     * @throws BeanException
     */
    void refresh() throws BeanException;

    /**
     * 关闭应用上下文
     */
    void close();

    /**
     * 向虚拟机中注册一个钩子方法，在虚拟机关闭之前执行关闭容器等操作
     */
    void registerShutdownHook();
}
