package org.jamie.mybatis.handler.type.impl;

import org.jamie.mybatis.handler.type.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author jamie
 * @version 1.0.0
 * @description Date类型处理器
 * @date 2023/01/02 07:40
 */
public class DateTypeHandler implements TypeHandler<Date> {

    @Override
    public void setParameter(PreparedStatement statement, int index, Date param) throws SQLException {
        java.sql.Date sqlDate = new java.sql.Date(param.getTime());
        statement.setDate(index, sqlDate);
    }

    @Override
    public Date getResult(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getDate(columnName);
    }
}
