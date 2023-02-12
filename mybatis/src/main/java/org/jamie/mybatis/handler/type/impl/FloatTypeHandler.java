package org.jamie.mybatis.handler.type.impl;

import org.jamie.mybatis.handler.type.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jamie
 * @version 1.0.0
 * @description Float类型处理器
 * @date 2023/01/02 07:40
 */
public class FloatTypeHandler implements TypeHandler<Float> {

    @Override
    public void setParameter(PreparedStatement statement, int index, Float param) throws SQLException {
        statement.setFloat(index, param);
    }

    @Override
    public Float getResult(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getFloat(columnName);
    }
}
