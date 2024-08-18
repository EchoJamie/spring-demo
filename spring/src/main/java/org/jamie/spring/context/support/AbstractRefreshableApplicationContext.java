package org.jamie.spring.context.support;

import org.jamie.spring.bean.support.DefaultListableBeanFactory;
import org.jamie.spring.exception.BeanException;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 09:30
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    protected final void refreshBeanFactory() throws BeanException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    public DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
