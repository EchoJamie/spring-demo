package org.jamie.demo.datasource.unpooled;

import org.jamie.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.jamie.mybatis.io.Resources;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.Reader;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 非池化数据源工厂 测试类
 * @date 2023/02/12 02:09
 */
public class UnpooledDataSourceFactoryTest {

    @Test
    public void setPropertiesTest() {
        String resourcePath = "application.properties";
        try (Reader reader = Resources.getResourceAsReader(resourcePath)) {
            Properties properties = new Properties();
            properties.load(reader);
            properties.forEach((k, v) -> {
                System.out.println(k + "=" + v);
            });

            UnpooledDataSourceFactory unpooledDataSourceFactory = new UnpooledDataSourceFactory();
            unpooledDataSourceFactory.setProperties(properties);
            DataSource dataSource = unpooledDataSourceFactory.getDataSource();
            Connection connection = dataSource.getConnection();
            System.out.println("connection.getAutoCommit(): " + connection.getAutoCommit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
