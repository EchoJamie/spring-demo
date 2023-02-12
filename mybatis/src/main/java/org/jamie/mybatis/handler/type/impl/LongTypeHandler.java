package org.jamie.mybatis.handler.type.impl;

import org.jamie.mybatis.handler.type.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jamie
 * @version 1.0.0
 * @description Long类型处理器
 * @date 2023/01/02 07:40
 */
public class LongTypeHandler implements TypeHandler<Long> {
    @Override
    public void setParameter(PreparedStatement statement, int index, Long param) throws SQLException {
        statement.setLong(index, param);
    }

    @Override
    public Long getResult(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getLong(columnName);
    }
}
