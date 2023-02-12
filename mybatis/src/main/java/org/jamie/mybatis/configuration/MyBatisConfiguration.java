package org.jamie.mybatis.configuration;

import org.jamie.mybatis.datasource.DataSourceFactory;
import org.jamie.mybatis.datasource.DataSourceFactoryBuilder;
import org.jamie.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.jamie.mybatis.io.Resources;
import org.jamie.mybatis.proxy.MapperProxyFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 本人设计的 MyBatis上下文 配置类 比较简陋
 * @date 2023/02/12 14:48
 */
public class MyBatisConfiguration implements Configuration{

    private Properties prop = new Properties();

    private DataSourceFactory dataSourceFactory;

    private Map<Class, MapperProxyFactory> mapperProxyFactoryMap = new ConcurrentHashMap<>();

    private Map<Class, Object> mapperCache = new ConcurrentHashMap<>();

    public MyBatisConfiguration(Properties properties) {
        prop.putAll(properties);
    }

    public MyBatisConfiguration(Reader reader) {
        try {
            prop.load(reader);
        } catch (Exception e) {
            throw new RuntimeException("properties load failed: " + reader, e);
        } finally {
            try {
                reader.close();
            } catch (IOException ignored) {
            }
        }
    }

    public MyBatisConfiguration(String propPath) {
        try (Reader reader = Resources.getResourceAsReader(propPath)){
            prop.load(reader);
        } catch (Exception e) {
            throw new RuntimeException("properties load failed: " + propPath, e);
        }
    }

    @Override
    public void setProperties(Properties properties) {
        this.prop = properties;
    }

    @Override
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override
    public DataSource getDataSource() {
        if (dataSourceFactory == null) {
            dataSourceFactory = DataSourceFactoryBuilder.build(prop);
        }
        return dataSourceFactory.getDataSource();
    }

    @Override
    public <T> MapperProxyFactory<T> getMapperProxyFactory(Class<T> mapperInterface) {
        if (!mapperProxyFactoryMap.containsKey(mapperInterface)) {
            registryMapper(mapperInterface);
        }
        return mapperProxyFactoryMap.get(mapperInterface);
    }

    @Override
    public void registryMapper(Class mapperInterface) {
        if (!mapperProxyFactoryMap.containsKey(mapperInterface)) {
            MapperProxyFactory mapperProxyFactory = new MapperProxyFactory(mapperInterface, this);
            mapperProxyFactoryMap.put(mapperInterface, mapperProxyFactory);
        }
        if (!mapperCache.containsKey(mapperInterface)) {
            MapperProxyFactory mapperProxyFactory = mapperProxyFactoryMap.get(mapperInterface);
            Object mapper = mapperProxyFactory.getMapper();
            mapperCache.put(mapperInterface, mapper);
        }
    }


}
