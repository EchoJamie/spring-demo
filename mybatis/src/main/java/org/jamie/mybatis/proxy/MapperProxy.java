package org.jamie.mybatis.proxy;

import org.jamie.mybatis.annotation.Param;
import org.jamie.mybatis.annotation.Select;
import org.jamie.mybatis.handler.token.ParameterMappingTokenHandler;
import org.jamie.mybatis.handler.type.TypeHandler;
import org.jamie.mybatis.handler.type.impl.*;
import org.jamie.mybatis.parser.GenericTokenParser;
import org.jamie.mybatis.parser.ParameterMapping;

import javax.sql.DataSource;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 映射代理类
 * @date 2023/02/12 13:46
 */
public class MapperProxy implements InvocationHandler {

    private final DataSource dataSource;

    private static Map<Class, TypeHandler> typeHandlerMap = new HashMap<>();

    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

    static {
        typeHandlerMap.put(Long.class, new LongTypeHandler());
        typeHandlerMap.put(Integer.class, new IntegerTypeHandler());
        typeHandlerMap.put(Short.class, new ShortTypeHandler());
        typeHandlerMap.put(Float.class, new FloatTypeHandler());
        typeHandlerMap.put(Double.class, new DoubleTypeHandler());
        typeHandlerMap.put(String.class, new StringTypeHandler());
        typeHandlerMap.put(Date.class, new DateTypeHandler());
    }

    public MapperProxy(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!methodCache.containsKey(method)) {
            methodCache.put(method, new MapperMethod(method));
        }
        return methodCache.get(method).execute(getConnection(), args);
    }
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
