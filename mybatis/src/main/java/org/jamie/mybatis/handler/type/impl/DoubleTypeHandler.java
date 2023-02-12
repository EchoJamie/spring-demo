package org.jamie.mybatis.handler.type.impl;

import org.jamie.mybatis.handler.type.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jamie
 * @version 1.0.0
 * @description Double类型处理器
 * @date 2023/01/02 07:40
 */
public class DoubleTypeHandler implements TypeHandler<Double> {

    @Override
    public void setParameter(PreparedStatement statement, int index, Double param) throws SQLException {
        statement.setDouble(index, param);
    }

    @Override
    public Double getResult(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getDouble(columnName);
    }
}
