package org.jamie.springboot.webserver.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author jamie
 * @version 1.0.0
 * @description Tomcat 依赖存在条件判断
 * @date 2023/01/01 01:21
 */
public class TomcatCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        // 判断条件
        try {
            conditionContext.getClassLoader().loadClass("com.apache.catalina.startup.Tomcat");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
