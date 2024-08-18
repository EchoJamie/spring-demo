package org.jamie.spring.bean.config;

import org.jamie.spring.bean.DisposableBean;
import org.jamie.spring.exception.BeanException;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:47
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final ConcurrentHashMap<String, Object> singletonPool = new ConcurrentHashMap<>();

    private final Map<String, DisposableBean> disposableBeans = new ConcurrentHashMap<>();

    /**
     * @param beanName
     * @return
     */
    @Override
    public Object getSingleton(String beanName) {
        return singletonPool.get(beanName);
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    /**
     * @param beanName
     * @param singletonObject
     */
    @Override
    public void addSingleton(String beanName, Object singletonObject) {
        singletonPool.put(beanName, singletonObject);
    }

    public void destroySingletons() {
        ArrayList<String> beanNames = new ArrayList<>(disposableBeans.keySet());
        for (String beanName : beanNames) {
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeanException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }
}
