package org.jamie.spring.context.impl;

import org.jamie.spring.annotation.ComponentScan;
import org.jamie.spring.bean.support.DefaultListableBeanFactory;
import org.jamie.spring.context.support.AbstractRefreshableApplicationContext;
import org.jamie.spring.context.support.ClassPathBeanDefinitionScanner;
import org.jamie.spring.exception.BeanException;

/**
 * @author jamie
 * @version 1.0.0
 * @description 自定义应用上下文
 * @date 2022/12/29 22:38
 */
public class JamieApplicationContext extends AbstractRefreshableApplicationContext {

    private final Class<?> configClass;

    private final ClassLoader classLoader = JamieApplicationContext.class.getClassLoader();

    public JamieApplicationContext(Class configClass) {
        super();
        this.configClass = configClass;
        refresh();
    }

    ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * @param requiredType
     * @param <T>
     * @return
     * @throws BeanException
     */
    @Override
    public <T> T getBean(Class<T> requiredType) throws BeanException {
        return getBeanFactory().getBean(requiredType);
    }

    /**
     * @param name
     * @return
     */
    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    /**
     * @param beanFactory
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        ComponentScan componentScanAnnotation = this.configClass.getDeclaredAnnotation(ComponentScan.class);
        // 获取扫描路径
        String[] scanPath = componentScanAnnotation.value();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        scanner.doScan(scanPath);
    }
}
