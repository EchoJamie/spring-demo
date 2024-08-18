package org.jamie.spring.bean.support;

import lombok.Setter;
import org.jamie.spring.bean.BeanDefinition;
import org.jamie.spring.bean.ConfigurableListableBeanFactory;
import org.jamie.spring.bean.annotation.Autowired;
import org.jamie.spring.exception.BeanException;
import org.jamie.spring.exception.BeanNotFoundException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 01:12
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory, BeanDefinitionRegistry {

    @Setter
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * @param beanDefinition
     * @param beanName
     * @return
     * @throws BeanException
     */
    @Override
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName) throws BeanException {
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = instantiationStrategy.instantiate(beanDefinition);

            // 依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Autowired autowiredAnnotation = declaredField.getDeclaredAnnotation(Autowired.class);
                    Object bean = getBean(declaredField.getName());
                    if (autowiredAnnotation.required() && bean == null) {
                        throw new BeanNotFoundException("依赖注入失败，未找到 %s", declaredField.getName());
                    }
                    declaredField.setAccessible(true);
                    declaredField.set(instance, bean);
                }
            }

            return instance;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向注册表中注BeanDefinition
     *
     * @param beanName
     * @param beanDefinition
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        if (containsBeanDefinition(beanName)) {
            // TODO check the same beanName
        }
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    /**
     * 根据名称查找BeanDefinition
     *
     * @param beanName
     * @return
     * @throws BeanException 如果找不到BeanDefintion
     */
    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeanException {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (Objects.nonNull(beanDefinition)) {
                return beanDefinition;
            }
        }

        throw new BeanNotFoundException("未找到 %s", beanName);
    }

    /**
     * 提前实例化所有单例实例
     *
     * @throws BeanException
     */
    @Override
    public void preInstantiateSingletons() throws BeanException {
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            //只有当bean是单例且不为懒加载才会被创建
            if (beanDefinition.isSingleton() && !beanDefinition.isLazyInit()) {
                getBean(beanName);
            }
        });
    }

    /**
     * 是否包含指定名称的BeanDefinition
     *
     * @param beanName
     * @return
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    /**
     * 返回指定类型的所有实例
     *
     * @param type
     * @return
     * @throws BeanException
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeanException {
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class beanClass = beanDefinition.getClazz();
            if (type.isAssignableFrom(beanClass)) {
                T bean = (T) getBean(beanName);
                result.put(beanName, bean);
            }
        });
        return result;
    }

    /**
     * 返回定义的所有bean的名称
     *
     * @return
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }


    /**
     *
     */
    @Override
    public void destroySingletons() {
        super.destroySingletons();
    }

    /**
     * @param requiredType
     * @param <T>
     * @return
     * @throws BeanException
     */
    @Override
    public <T> T getBean(Class<T> requiredType) throws BeanException {
        List<String> beanNames = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class<?> beanClass = entry.getValue().getClazz();
            if (requiredType.isAssignableFrom(beanClass)) {
                beanNames.add(entry.getKey());
            }
        }
        if (beanNames.size() == 1) {
            return getBean(beanNames.get(0), requiredType);
        }

        throw new BeanException(requiredType + "expected single bean but found " +
                beanNames.size() + ": " + beanNames);

    }

    /**
     * @param name
     * @return
     */
    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }
}
