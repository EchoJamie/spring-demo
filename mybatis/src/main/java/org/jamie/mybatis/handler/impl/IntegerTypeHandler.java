package org.jamie.mybatis.handler.impl;

import org.jamie.mybatis.handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jamie
 * @version 1.0.0
 * @description Integer类型处理器
 * @date 2023/01/02 07:40
 */
public class IntegerTypeHandler implements TypeHandler<Integer> {
    @Override
    public void setParameter(PreparedStatement statement, int index, Integer param) throws SQLException {
        statement.setInt(index, param);
    }

    @Override
    public Integer getResult(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getInt(columnName);
    }
}
