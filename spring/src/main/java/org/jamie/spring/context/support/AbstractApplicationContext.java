package org.jamie.spring.context.support;

import org.jamie.spring.bean.BeanPostProcessor;
import org.jamie.spring.bean.ConfigurableListableBeanFactory;
import org.jamie.spring.bean.config.BeanFactoryPostProcessor;
import org.jamie.spring.bean.support.ApplicationContextAwareProcessor;
import org.jamie.spring.bean.support.DefaultListableBeanFactory;
import org.jamie.spring.context.ApplicationEvent;
import org.jamie.spring.context.ApplicationEventMulticaster;
import org.jamie.spring.context.ApplicationListener;
import org.jamie.spring.context.SimpleApplicationEventMulticaster;
import org.jamie.spring.context.event.ContextClosedEvent;
import org.jamie.spring.context.event.ContextRefreshedEvent;
import org.jamie.spring.core.ConversionService;
import org.jamie.spring.exception.BeanException;
import org.jamie.spring.resource.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 01:06
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
    public static final String CONVERSION_SERVICE_BEAN_NAME = "conversionService";
    private final DefaultListableBeanFactory beanFactory;

    private ApplicationEventMulticaster applicationEventMulticaster;

    public AbstractApplicationContext() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    public AbstractApplicationContext(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void refresh() throws BeanException {
        //创建BeanFactory，并加载BeanDefinition
        refreshBeanFactory();
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        //添加ApplicationContextAwareProcessor，让继承自ApplicationContextAware的bean能感知bean
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        //在bean实例化之前，执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        //BeanPostProcessor需要提前与其他bean实例化之前注册
        registerBeanPostProcessors(beanFactory);

        //初始化事件发布者
        initApplicationEventMulticaster();

        //注册事件监听器
        registerListeners();

        //注册类型转换器和提前实例化单例bean
        finishBeanFactoryInitialization(beanFactory);

        //发布容器刷新完成事件
        finishRefresh();
    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    private void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
//设置类型转换器
        if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME)) {
            Object conversionService = beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME);
            if (conversionService instanceof ConversionService) {
                beanFactory.setConversionService((ConversionService) conversionService);
            }
        }

        //提前实例化单例bean
        beanFactory.preInstantiateSingletons();
    }

    private void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener applicationListener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(applicationListener);
        }
    }

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.addSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    protected abstract void refreshBeanFactory() throws BeanException;

    /**
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
        return getBeanFactory().getBean(beanName);
    }

    /**
     * @param beanName
     * @param requiredType
     * @return
     */
    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) {
        return getBeanFactory().getBean(beanName, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeanException {
        return getBeanFactory().getBean(requiredType);
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
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public void close() {
        doClose();
    }

    protected void doClose() {
        //发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        //执行单例bean的销毁方法
        destroyBeans();
    }

    private void destroyBeans() {
        getBeanFactory().destroySingletons();
    }

    /**
     * 返回定义的所有bean的名称
     *
     * @return
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return beanFactory.getBeanDefinitionNames();
    }

    @Override
    public void registerShutdownHook() {
        Thread shutdownHook = new Thread() {
            public void run() {
                doClose();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    /**
     * 发布事件
     *
     * @param event
     */
    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }
}
