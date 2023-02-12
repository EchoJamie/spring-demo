package org.jamie.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 数据源工厂
 * @date 2023/02/11 15:36
 */
public interface DataSourceFactory {

    void setProperties(Properties prop);

    DataSource getDataSource();

}
