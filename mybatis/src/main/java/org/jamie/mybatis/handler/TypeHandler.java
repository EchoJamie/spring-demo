package org.jamie.mybatis.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jamie
 * @version 1.0.0
 * @description 类型处理器接口
 * @date 2023/01/02 07:38
 */
public interface TypeHandler<T> {

    /**
     * 给PreparedStatement设置参数
     * @param statement
     * @param index
     * @param param
     * @throws SQLException
     */
    void setParameter(PreparedStatement statement, int index, T param) throws SQLException;

    T getResult(ResultSet resultSet, String columnName) throws SQLException;
}
