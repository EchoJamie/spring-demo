package org.jamie.springboot.webserver.condition;

import org.jamie.springboot.annotation.JamieConditionalOnClass;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @author jamie
 * @version 1.0.0
 * @description 类依赖 判断接口实现
 * @date 2023/01/01 01:41
 */
public class ClassCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        // 判断条件
        Map<String, Object> annotationAttributes = annotatedTypeMetadata.getAnnotationAttributes(JamieConditionalOnClass.class.getName());
        String className = (String) annotationAttributes.get("value");
        try {
            conditionContext.getClassLoader().loadClass(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
