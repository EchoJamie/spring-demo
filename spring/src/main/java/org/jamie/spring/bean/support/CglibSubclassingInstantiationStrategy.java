package org.jamie.spring.bean.support;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.jamie.spring.bean.BeanDefinition;
import org.jamie.spring.exception.BeanException;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 01:45
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {
    /**
     * @param beanDefinition
     * @return
     * @throws BeanException
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeanException {
        Enhancer enhancer = new Enhancer();
        Class<?> clazz = beanDefinition.getClazz();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> method.invoke(obj, args));
        return enhancer.create();
    }
}
