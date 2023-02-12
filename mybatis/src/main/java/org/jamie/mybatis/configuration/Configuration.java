package org.jamie.mybatis.configuration;

import org.jamie.mybatis.datasource.DataSourceFactory;
import org.jamie.mybatis.proxy.MapperProxyFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 配置接口  完全由本人设计
 *              若要学习建议建议参考 Spring上下文类
 * @date 2023/02/12 14:53
 */
public interface Configuration {

    void setDataSourceFactory(DataSourceFactory dataSourceFactory);

    DataSource getDataSource();

    /**
     * 懒加载方式 获取一个或者注册一个 则生成一个
     * @param mapperInterface
     * @return
     */
    <T> MapperProxyFactory<T> getMapperProxyFactory(Class<T> mapperInterface);

    void setProperties(Properties properties);

    void registryMapper(Class mapperInterface);

    public default Configuration getConfiguration() {
        return this;
    }
}
