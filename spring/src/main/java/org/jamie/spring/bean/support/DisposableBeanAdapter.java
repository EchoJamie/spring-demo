package org.jamie.spring.bean.support;

import org.jamie.spring.bean.BeanDefinition;
import org.jamie.spring.bean.DisposableBean;
import org.jamie.spring.exception.BeanException;
import org.jamie.spring.util.ReflectUtils;
import org.jamie.spring.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/26 00:08
 */
public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;

    private final String beanName;

    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    /**
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        //避免同时继承自DisposableBean，且自定义方法与DisposableBean方法同名，销毁方法执行两次的情况
        if (StringUtils.isNotEmpty(destroyMethodName) && !(bean instanceof DisposableBean && "destroy".equals(destroyMethodName))) {
            //执行自定义方法
            ReflectUtils.invokePublic(bean, destroyMethodName, (e) -> {
                throw new BeanException("Couldn't find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
            });
        }
    }
}
