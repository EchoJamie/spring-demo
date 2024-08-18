package org.jamie.spring.aop;

/**
 * 被代理对象的封装
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/26 01:28
 */
public class TargetSource {

    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    public Class<?>[] getTargetClass() {
        return this.target.getClass().getInterfaces();
    }

    public Object getTarget() {
        return this.target;
    }
}
