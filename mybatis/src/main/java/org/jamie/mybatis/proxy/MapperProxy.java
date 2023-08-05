package org.jamie.mybatis.proxy;

import javax.sql.DataSource;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 映射代理类
 * @date 2023/02/12 13:46
 */
public class MapperProxy implements InvocationHandler {

    private final DataSource dataSource;

    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

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
