package org.jamie.pool.proxy;

import org.jamie.pool.datasource.PooledJamieDatasourceImpl;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author jamie
 * @version 1.0.0
 * @description 连接 动态代理
 * @date 2023/7/10 01:00
 */
@Setter
@Getter
public class ConnectionProxy implements InvocationHandler {

    /**
     * 真实连接
     */
    private Connection realConnection;
    /**
     * 代理连接
     */
    private Connection proxyConnection;
    /**
     * 数据源
     */
    private PooledJamieDatasourceImpl datasource;

    public ConnectionProxy(Connection realConnection, PooledJamieDatasourceImpl datasource) {
        // 通过构造方法 初始化 真实连接及数据源
        this.realConnection = realConnection;
        this.datasource = datasource;

        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();
        if (methodName.equalsIgnoreCase("close")) {
            // 把连接 归还到连接池
            datasource.closeConnection(this);
            return null;
        }

        return method.invoke(realConnection, args);
    }
}
