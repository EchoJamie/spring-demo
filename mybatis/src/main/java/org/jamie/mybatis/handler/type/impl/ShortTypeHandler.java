package org.jamie.mybatis.handler.type.impl;

import org.jamie.mybatis.handler.type.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jamie
 * @version 1.0.0
 * @description Short类型处理器
 * @date 2023/01/02 07:40
 */
public class ShortTypeHandler implements TypeHandler<Short> {
    @Override
    public void setParameter(PreparedStatement statement, int index, Short param) throws SQLException {
        statement.setShort(index, param);
    }

    @Override
    public Short getResult(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getShort(columnName);
    }
}
