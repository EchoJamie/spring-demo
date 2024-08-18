package org.jamie.spring.aop.framework;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/26 01:35
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    private final Object target;

    private final Method method;

    private final Object[] arguments;

    public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }

    /**
     * @return
     */
    @Override
    public Method getMethod() {
        return method;
    }

    /**
     * @return
     */
    @Override
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * @return
     * @throws Throwable
     */
    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, arguments);
    }

    /**
     * @return
     */
    @Override
    public Object getThis() {
        return target;
    }

    /**
     * @return
     */
    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }
}
