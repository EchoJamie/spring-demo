package org.jamie.mybatis.datasource;

import org.jamie.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

import java.util.Properties;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 数据源工厂 建造者 简单实现 近出于好用
 * @date 2023/02/12 18:34
 */
public class DataSourceFactoryBuilder {

    private static DataSourceFactory dataSourceFactory;

    public static DataSourceFactory build(Properties properties) {
        if (dataSourceFactory == null) {
            dataSourceFactory = new UnpooledDataSourceFactory();
        }
        dataSourceFactory.setProperties(properties);
        return dataSourceFactory;
    }
}
