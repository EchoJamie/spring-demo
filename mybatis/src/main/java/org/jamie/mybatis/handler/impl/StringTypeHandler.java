package org.jamie.mybatis.handler.impl;

import org.jamie.mybatis.handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jamie
 * @version 1.0.0
 * @description String类型处理器
 * @date 2023/01/02 07:40
 */
public class StringTypeHandler implements TypeHandler<String> {
    @Override
    public void setParameter(PreparedStatement statement, int index, String param) throws SQLException {
        statement.setString(index, param);
    }

    @Override
    public String getResult(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getString(columnName);
    }
}
