package org.jamie.spring.bean.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jamie.spring.bean.BeanDefinition;
import org.jamie.spring.bean.config.BeanReference;
import org.jamie.spring.bean.support.AbstractBeanDefinitionReader;
import org.jamie.spring.bean.support.BeanDefinitionRegistry;
import org.jamie.spring.context.support.ClassPathBeanDefinitionScanner;
import org.jamie.spring.env.PropertyValue;
import org.jamie.spring.exception.BeanException;
import org.jamie.spring.resource.Resource;
import org.jamie.spring.resource.ResourceLoader;
import org.jamie.spring.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 05:27
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String SCOPE_ATTRIBUTE = "scope";
    public static final String LAZYINIT_ATTRIBUTE = "lazyInit";
    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    public static final String COMPONENT_SCAN_ELEMENT = "component-scan";

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        super(beanDefinitionRegistry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry, ResourceLoader resourceLoader) {
        super(beanDefinitionRegistry, resourceLoader);
    }

    /**
     * @param resource
     * @throws BeanException
     */
    @Override
    public void loadBeanDefinitions(Resource resource) throws BeanException {
        try {
            InputStream inputStream = resource.getInputStream();
            try {
                doLoadBeanDefinitions(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException | DocumentException ex) {
            throw new BeanException("IOException parsing XML document from " + resource, ex);
        }
    }

    private void doLoadBeanDefinitions(InputStream inputStream) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document doc = saxReader.read(inputStream);

        // root 元素
        Element rootElement = doc.getRootElement();
        // 组件扫描元素
        Element componentScan = rootElement.element(COMPONENT_SCAN_ELEMENT);
        if (Objects.nonNull(componentScan)) {
            String basePackage = componentScan.attributeValue(BASE_PACKAGE_ATTRIBUTE);
            if (StringUtils.isEmpty(basePackage)) {
                throw new BeanException("The value of base-package attribute can not be empty or null");
            }
            scanPackage(basePackage);
        }

        List<Element> beanList = rootElement.elements(BEAN_ELEMENT);
        for (Element bean : beanList) {
            String beanId = bean.attributeValue(ID_ATTRIBUTE);
            String beanName = bean.attributeValue(NAME_ATTRIBUTE);
            String className = bean.attributeValue(CLASS_ATTRIBUTE);
            String initMethodName = bean.attributeValue(INIT_METHOD_ATTRIBUTE);
            String destroyMethodName = bean.attributeValue(DESTROY_METHOD_ATTRIBUTE);
            String beanScope = bean.attributeValue(SCOPE_ATTRIBUTE);
            String lazyInit = bean.attributeValue(LAZYINIT_ATTRIBUTE);
            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new BeanException("Cannot find class [" + className + "]");
            }
            // id 优于 name
            beanName = StringUtils.isNotEmpty(beanId) ? beanId : beanName;
            if (StringUtils.isEmpty(beanName)) {
                //如果id和name都为空，将类名的第一个字母转为小写后作为bean的名称
                String simpleName = clazz.getSimpleName();
                beanName = StringUtils.firstLower(simpleName);
            }

            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);
            beanDefinition.setLazyInit(Boolean.parseBoolean(lazyInit));

            if (StringUtils.isNotEmpty(beanScope)) {
                beanDefinition.setScope(beanScope);
            }

            List<Element> propertyList = bean.elements(PROPERTY_ELEMENT);
            for (Element property : propertyList) {
                String propertyNameAttribute = property.attributeValue(NAME_ATTRIBUTE);
                String propertyValueAttribute = property.attributeValue(VALUE_ATTRIBUTE);
                String propertyRefAttribute = property.attributeValue(REF_ATTRIBUTE);

                if (StringUtils.isEmpty(propertyNameAttribute)) {
                    throw new BeanException("The name attribute cannot be null or empty");
                }

                Object value = propertyValueAttribute;
                if (StringUtils.isNotEmpty(propertyRefAttribute)) {
                    value = new BeanReference(propertyRefAttribute);
                }
                PropertyValue propertyValue = new PropertyValue(propertyNameAttribute, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)) {
                //beanName不能重名
                throw new BeanException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            //注册BeanDefinition
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }

    }

    private void scanPackage(String basePackage) {
        String[] basePackages = null;
        if (basePackage.contains(",")) {
            basePackages = basePackage.split(",");
        } else {
            basePackages = new String[]{basePackage};
        }
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackages);
    }

    /**
     * @param location
     * @throws BeanException
     */
    @Override
    public void loadBeanDefinitions(String location) throws BeanException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }
}
