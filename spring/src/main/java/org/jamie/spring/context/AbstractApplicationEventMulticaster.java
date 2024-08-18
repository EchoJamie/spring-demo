package org.jamie.spring.context;

import org.jamie.spring.bean.BeanFactory;
import org.jamie.spring.bean.BeanFactoryAware;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 09:49
 */
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    private BeanFactory beanFactory;

    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new HashSet<>();

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
