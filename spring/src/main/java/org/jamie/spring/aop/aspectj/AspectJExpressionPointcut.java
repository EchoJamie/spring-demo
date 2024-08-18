package org.jamie.spring.aop.aspectj;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.jamie.spring.aop.ClassFilter;
import org.jamie.spring.aop.MethodMatcher;
import org.jamie.spring.aop.Pointcut;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 表单式切点
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/26 01:18
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {

    // 切点 原语
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

    static {
        // 支持切点表达式
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }

    private final PointcutExpression pointcutExpression;

    public AspectJExpressionPointcut(String expression) {
        // 获取切点解析器 支持指定原语 并且 使用指定的类加载器
        PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
        // 解析起 解析切点表达式
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}
