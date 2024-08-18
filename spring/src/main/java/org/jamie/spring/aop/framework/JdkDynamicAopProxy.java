package org.jamie.spring.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.jamie.spring.aop.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/26 01:27
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    /**
     * @return
     */
    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(), advised.getTargetSource().getTargetClass(), this);
    }

    /**
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the {@code Method} instance corresponding to
     *               the interface method invoked on the proxy instance.  The declaring
     *               class of the {@code Method} object will be the interface that
     *               the method was declared in, which may be a superinterface of the
     *               proxy interface that the proxy class inherits the method through.
     * @param args   an array of objects containing the values of the
     *               arguments passed in the method invocation on the proxy instance,
     *               or {@code null} if interface method takes no arguments.
     *               Arguments of primitive types are wrapped in instances of the
     *               appropriate primitive wrapper class, such as
     *               {@code java.lang.Integer} or {@code java.lang.Boolean}.
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method, args));
        }
        return method.invoke(proxy, args);
    }
}
