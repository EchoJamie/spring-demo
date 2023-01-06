package org.jamie.spring.context.impl;

import org.jamie.spring.annotation.Component;
import org.jamie.spring.annotation.ComponentScan;
import org.jamie.spring.bean.BeanDefinition;
import org.jamie.spring.bean.BeanNameAware;
import org.jamie.spring.bean.BeanPostProcessor;
import org.jamie.spring.bean.InitializingBean;
import org.jamie.spring.bean.annotation.Autowired;
import org.jamie.spring.bean.annotation.Scope;
import org.jamie.spring.bean.constant.ScopeConstant;
import org.jamie.spring.bean.exception.BeanNotFoundException;
import org.jamie.spring.context.ApplicationContext;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jamie
 * @version 1.0.0
 * @description 自定义应用上下文
 * @date 2022/12/29 22:38
 */
public class JamieApplicationContext implements ApplicationContext {

    private Class configClass;

    private ConcurrentHashMap<String, Object> singletonPool = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    private ClassLoader classLoader = JamieApplicationContext.class.getClassLoader();

    public JamieApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描Bean
        scan();

        // 创建Bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals(ScopeConstant.SINGLETON)) {
                Object bean = createBean(beanDefinition, beanName);
                singletonPool.put(beanName, bean);
            }
        }
    }

    private Object createBean(BeanDefinition beanDefinition, String beanName) {
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();

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

            // Aware回调  BeanNameAware接口
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            // BeanPostProcessor 初始化前
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                 instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }


            // Aware回调  初始化
            if (instance instanceof InitializingBean) {
                try {
                    ((InitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // BeanPostProcessor 初始化后
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void scan() {
        ComponentScan componentScanAnnotation = (ComponentScan) this.configClass.getDeclaredAnnotation(ComponentScan.class);
        // 获取扫描路径
        String[] scanPath = componentScanAnnotation.value();
        for (String path : scanPath) {
            // 开始扫描
            URL resource = classLoader.getResource(path.replace(".", "/"));
            File directory = new File(resource.getFile());
            // 确定声明的路径为目录
            if (directory.isDirectory()) {
                scanBeanByPackage(directory);
            }
        }
    }

    private void scanBeanByPackage(File directory) {
        // 遍历文件列表
        File[] files = directory.listFiles();
        for (File file : files) {
            String fileName = file.getAbsolutePath();
            // 判断是java class文件
            if (fileName.endsWith(".class")) {
                String className = fileName.substring(fileName.indexOf("org"), fileName.indexOf(".class")).replace("/", ".");
                try {
                    Class<?> loadClass = classLoader.loadClass(className);
                    if (loadClass.isAnnotationPresent(Component.class)) {
                        // 确认是bean

                        // 处理BeanPostProcessor 实际框架中不是在这里处理的
                        if (BeanPostProcessor.class.isAssignableFrom(loadClass)) {
                            BeanPostProcessor beanPostProcessor = (BeanPostProcessor) loadClass.getDeclaredConstructor().newInstance();
                            beanPostProcessorList.add(beanPostProcessor);
                        }

                        Component componentAnnotation = loadClass.getDeclaredAnnotation(Component.class);
                        String beanName = componentAnnotation.value();
                        // 创建BeanDefinition
                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setClazz(loadClass);
                        // 判断单例或原型模式
                        if (loadClass.isAnnotationPresent(Scope.class)) {
                            Scope scopeAnnotation = loadClass.getDeclaredAnnotation(Scope.class);
                            beanDefinition.setScope(scopeAnnotation.value());
                        } else {
                            beanDefinition.setScope(ScopeConstant.SINGLETON);
                        }
                        // 将BeanDefinition放入BeanDefinitionMap
                        beanDefinitionMap.put(beanName, beanDefinition);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            // 逐级目录扫描
            if (file.isDirectory()) {
                scanBeanByPackage(file);
            }
        }
    }

    @Override
    public Object getBean(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals(ScopeConstant.SINGLETON)) {
                // 单例模式
                Object o = singletonPool.get(beanName);
                return o;
            } else {
                // 原型模式
                Object bean = createBean(beanDefinition, beanName);
                return bean;
            }
        } else {
            throw new BeanNotFoundException("未找到 %s", beanName);
        }
    }

    @Override
    public Object getBean(Class clazz) {
        return null;
    }
}
