package org.jamie.mybatis.reflection;

import java.lang.reflect.Field;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description Java 元对象 提供反射方法
 * @date 2023/02/12 02:43
 */
public class MetaJavaObject<T> {

    private Class<T> metaClazz;

    private T instance;

    private static final String SETTER_PREFIX = "set";

    public MetaJavaObject(Class<T> metaClazz) {
        this.metaClazz = metaClazz;
    }

    public MetaJavaObject(T metaObject) {
        this.instance = metaObject;
        this.metaClazz = (Class<T>) metaObject.getClass();
    }

    /**
     * 是否存在Setter方法
     * 反射方式实现
     * @param attributeName
     * @return
     */
    public boolean containSetterMethod(String attributeName) {
        try {
            Field field = metaClazz.getDeclaredField(attributeName);
            metaClazz.getMethod(SETTER_PREFIX + toUpperCamelCase(attributeName), field.getType());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Class getSetterType(String attributeName) {
        try {
            return metaClazz.getDeclaredField(attributeName).getType();
        } catch (NoSuchFieldException e) {
            // TODO 暂未想到好的设计
            throw new RuntimeException(e);
        }
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        if (instance == null) {
            // 应该抛出异常
            System.out.println("Can't setAttribute, Because no instance of " + metaClazz.getSimpleName());
            return;
        }
        try {
            Field declaredField = metaClazz.getDeclaredField(attributeName);
            declaredField.setAccessible(true);
            declaredField.set(instance, attributeValue);
        } catch (Exception ignored) { // 值未设置
        }
    }

    private String toLowerCamelCase(String upperCamelCase) {
        return upperCamelCase.substring(0, 1).toLowerCase() + upperCamelCase.substring(1);
    }

    private String toUpperCamelCase(String lowerCamelCase) {
        return lowerCamelCase.substring(0, 1).toUpperCase() + lowerCamelCase.substring(1);
    }
}
