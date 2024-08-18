package org.jamie.spring.util;

import org.jamie.spring.exception.BeanException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 06:58
 */
public class ReflectUtils {

    public static void setField(Object obj, String fieldName, Object value) {
        Class<?> clazz = obj.getClass();

        String setMethodName = "set" + StringUtils.firstUpper(fieldName);
        try {
            Field field = clazz.getDeclaredField(fieldName);
            Method setMethod = clazz.getDeclaredMethod(setMethodName, field.getType());
            boolean methodAccessible = setMethod.isAccessible();
            setMethod.setAccessible(true);
            try {
                setMethod.invoke(obj, value);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                setMethod.setAccessible(methodAccessible);
            }
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void invokePublic(Object obj, String methodName, Consumer<ReflectiveOperationException> exceptionConsumer, Object... args) {
        try {
            Class<?> clazz = obj.getClass();
            Class<?>[] argsClass = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
            Method method = clazz.getMethod(methodName, argsClass);
            if (method.isAccessible()) {
                method.invoke(obj, args);
            }
        } catch (ReflectiveOperationException e) {
            exceptionConsumer.accept(e);
        }
    }
}
