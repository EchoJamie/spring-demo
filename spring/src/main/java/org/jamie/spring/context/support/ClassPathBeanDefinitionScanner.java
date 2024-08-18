package org.jamie.spring.context.support;

import org.jamie.spring.annotation.Component;
import org.jamie.spring.bean.BeanDefinition;
import org.jamie.spring.bean.annotation.AutowiredAnnotationBeanPostProcessor;
import org.jamie.spring.bean.annotation.Scope;
import org.jamie.spring.bean.support.BeanDefinitionRegistry;
import org.jamie.spring.util.StringUtils;

import java.util.Set;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 06:30
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.jamie.spring.bean.config.InstantiationAwareBeanPostProcessor";

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                // 解析bean的作用域
                String beanScope = resolveBeanScope(candidateComponent);
                if (StringUtils.isNotEmpty(beanScope)) {
                    candidateComponent.setScope(beanScope);
                }
                //生成bean的名称
                String beanName = determineBeanName(candidateComponent);
                //注册BeanDefinition
                registry.registerBeanDefinition(beanName, candidateComponent);
            }
        }
        registry.registerBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME, new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getClazz();
        Component component = clazz.getDeclaredAnnotation(Component.class);
        String name = component.value();
        if (StringUtils.isEmpty(name)) {
            String simpleName = clazz.getSimpleName();
            name = StringUtils.firstLower(simpleName);
        }
        return name;
    }

    private String resolveBeanScope(BeanDefinition candidateComponent) {
        Class<?> clazz = candidateComponent.getClazz();
        Scope scope = clazz.getDeclaredAnnotation(Scope.class);
        if (scope != null) {
            return scope.value();
        }
        return "";
    }
}
