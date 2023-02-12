package org.jamie.mybatis.datasource.pooled;

import org.jamie.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 池化数据源工厂
 * @date 2023/02/11 15:44
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    public PooledDataSourceFactory() {
        this.dataSource = new PooledDataSource();
    }
}
