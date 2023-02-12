package org.jamie.mybatis.proxy;

import org.jamie.mybatis.configuration.Configuration;

import javax.sql.DataSource;
import java.lang.reflect.*;

/**
 * @author jamie
 * @version 1.0.0
 * @description 映射代理工厂
 * @date 2023/01/02 05:50
 */
public class MapperProxyFactory<T> {

    private Configuration configuration;

    private Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface, Configuration configuration) {
        this.mapperInterface = mapperInterface;
        this.configuration = configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public T getMapper() {
        return getMapper(mapperInterface, configuration.getDataSource());
    }

    public static <T> T getMapper(Class<T> mapper, DataSource dataSource) {
        Object proxyInstance = Proxy.newProxyInstance(mapper.getClassLoader(), new Class[]{mapper}, new MapperProxy(dataSource));
        return (T) proxyInstance;
    }
}
