package org.jamie.spring.aop;

import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/26 01:29
 */
@Getter
@Setter
public class AdvisedSupport {

    private TargetSource targetSource;

    private MethodInterceptor methodInterceptor;

    private MethodMatcher methodMatcher;


}
