package org.jamie.mybatis.datasource.unpooled;

import org.jamie.mybatis.datasource.DataSourceFactory;
import org.jamie.mybatis.reflection.MetaJavaObject;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 非池化数据源工厂
 * @date 2023/02/11 15:44
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {

    private static final String DRIVER_PROPERTY_PREFIX = "driver.";
    private static final int DRIVER_PROPERTY_PREFIX_LENGTH = DRIVER_PROPERTY_PREFIX.length();
    protected DataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public void setProperties(Properties prop) {
        Properties driverProperties = new Properties();
        MetaJavaObject metaDataSource = new MetaJavaObject(dataSource);
        for (Object key : prop.keySet()) {
            String propertyName = (String) key;
            if (propertyName.startsWith(DRIVER_PROPERTY_PREFIX)) {
                String propertyValue = prop.getProperty(propertyName);
                driverProperties.put(propertyName.substring(DRIVER_PROPERTY_PREFIX_LENGTH), propertyValue);
            } else if (metaDataSource.containSetterMethod(propertyName)) {
                String propertyValue = prop.getProperty(propertyName);
                metaDataSource.setAttribute(propertyName, convertValue(metaDataSource, propertyName, propertyValue));
            }
        }
        if (driverProperties.size() > 0) {
            metaDataSource.setAttribute("driverProperties", driverProperties);
        }
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    private Object convertValue(MetaJavaObject metaDataSource, String attributeName, String value) {
        Object convertedValue = value;
        Class targetType = metaDataSource.getSetterType(attributeName);
        if (targetType == Integer.class || targetType == int.class) {
            convertedValue = Integer.valueOf(value);
        } else if (targetType == Long.class || targetType == long.class) {
            convertedValue = Long.valueOf(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            convertedValue = Boolean.valueOf(value);
        }
        return convertedValue;
    }
}
