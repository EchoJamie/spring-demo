package org.jamie.mybatis.proxy;

import org.jamie.mybatis.annotation.*;
import org.jamie.mybatis.handler.token.ParameterMappingTokenHandler;
import org.jamie.mybatis.handler.type.TypeHandler;
import org.jamie.mybatis.handler.type.impl.*;
import org.jamie.mybatis.parser.GenericTokenParser;
import org.jamie.mybatis.parser.ParameterMapping;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static org.jamie.mybatis.proxy.MethodType.*;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 映射方法 代理类
 * @date 2023/02/12 14:41
 */
public class MapperMethod {

    private final Method method;

    private MethodType methodType;

    private String sql;

    private String parseSql;

    private Class resultType;

    List<ParameterMapping> parameterMappings;
    Map<String, Method> ResultTypeSetterMethodMap = new HashMap<>();

    private static Map<Class, TypeHandler> typeHandlerMap = new HashMap<>();

    static {
        typeHandlerMap.put(Long.class, new LongTypeHandler());
        typeHandlerMap.put(Integer.class, new IntegerTypeHandler());
        typeHandlerMap.put(Short.class, new ShortTypeHandler());
        typeHandlerMap.put(Float.class, new FloatTypeHandler());
        typeHandlerMap.put(Double.class, new DoubleTypeHandler());
        typeHandlerMap.put(String.class, new StringTypeHandler());
        typeHandlerMap.put(Date.class, new DateTypeHandler());
    }

    private final ParameterMappingTokenHandler TOKEN_HANDLER = new ParameterMappingTokenHandler();
    private final GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", TOKEN_HANDLER);

    public MapperMethod(Method method) {
        this.method = method;
        resolveAnnotation();
        resolveReturnType();
    }

    public Object execute(Connection connection, Object[] args) throws SQLException {
        switch (this.methodType) {
            case SELECT:
                // TODO 感觉这么写很丑 垃圾代码
                Map<String, Object> paramValueMap = resolveParamValue(args);
                PreparedStatement preparedStatement = connection.prepareStatement(parseSql);
                for (int i = 0; i < parameterMappings.size(); i++) {
                    String property = parameterMappings.get(i).getProperty();
                    Object value = paramValueMap.get(property);
                    Class<?> type = value.getClass();
                    typeHandlerMap.get(type).setParameter(preparedStatement, i + 1, value);
                }
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getResultSet();
                return resolveResultSet(resultSet);
                // TODO 待补充 INSERT UPDATE DELETE 逻辑
            case UNKNOWN:
            default:
                return null;
        }
    }

    private void resolveAnnotation() {
        if (this.method.isAnnotationPresent(Select.class)) {
            Select select = method.getAnnotation(Select.class);
            this.methodType = SELECT;
            this.sql = select.value();
        } else if (this.method.isAnnotationPresent(Insert.class)) {
            Insert insert = method.getAnnotation(Insert.class);
            this.methodType = INSERT;
            this.sql = insert.value();
        } else if (this.method.isAnnotationPresent(Update.class)) {
            Update update = method.getAnnotation(Update.class);
            this.methodType = UPDATE;
            this.sql = update.value();
        }else if (this.method.isAnnotationPresent(Delete.class)) {
            Delete delete = method.getAnnotation(Delete.class);
            this.methodType = DELETE;
            this.sql = delete.value();
        } else {
            // 无注解方法
            this.methodType = UNKNOWN;
        }
        if (this.sql != null) {
            parseSql = genericTokenParser.parse(sql);
            parameterMappings = TOKEN_HANDLER.getParameterMappings();
        }
    }

    private Map<String, Object> resolveParamValue(Object[] args) {
        Map<String, Object> paramValueMap = new HashMap();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String name = parameter.getAnnotation(Param.class).value();
            paramValueMap.put(parameter.getName(), args[i]);
            paramValueMap.put(name, args[i]);
        }
        return paramValueMap;
    }

    private void resolveReturnType() {
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof Class) {
            // 不是泛型
            resultType = (Class) genericReturnType;
        } else if (genericReturnType instanceof ParameterizedType) {
            // 是泛型
            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            resultType = (Class) actualTypeArguments[0];
        }
        for (Method declaredMethod : resultType.getDeclaredMethods()) {
            if (declaredMethod.getName().startsWith("set")) {
                String propertyName = declaredMethod.getName().substring(3);
                propertyName= propertyName.substring(0, 1).toLowerCase(Locale.ROOT) + propertyName.substring(1);
                ResultTypeSetterMethodMap.put(propertyName, declaredMethod);
            }
        }
    }

    private Object resolveResultSet(ResultSet resultSet) throws SQLException {
        Object result = null;
        List<Object> resultList = new ArrayList<>();
        // 获取结果集 字段
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<String> columnList = new ArrayList<>();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            columnList.add(metaData.getColumnName(i + 1));
        }
        while (resultSet.next()) {
            Object instance = null;
            try {
                instance = resultType.newInstance();
                for (String column : columnList) {
                    Method setterMethod = ResultTypeSetterMethodMap.get(column);
                    TypeHandler typeHandler = typeHandlerMap.get(setterMethod.getParameterTypes()[0]);
                    setterMethod.invoke(instance, typeHandler.getResult(resultSet, column));
                }
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            resultList.add(instance);
        }
        if (method.getReturnType().equals(List.class)) {
            result = resultList;
        } else {
            result = resultList.get(0);
        }
        return result;
    }

}
