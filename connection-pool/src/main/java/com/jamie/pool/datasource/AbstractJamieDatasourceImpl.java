package com.jamie.pool.datasource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

/**
 * @author jamie
 * @version 1.0.0
 * @description 抽象的数据源实现类 也可认为是非池化数据源
 * @date 2023/7/10 00:48
 */
@Setter
@Getter
@ToString
public class AbstractJamieDatasourceImpl implements JamieDatasource {

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url;
    private String username;
    private String password;

    /**
     * <p>Attempts to establish a connection with the data source that
     * this {@code DataSource} object represents.
     *
     * @return a connection to the data source
     * @throws SQLException        if a database access error occurs
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value specified by the {@code setLoginTimeout} method
     *                             has been exceeded and has at least tried to cancel the
     *                             current database connection attempt
     */
    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(username, password);
    }

    /**
     * <p>Attempts to establish a connection with the data source that
     * this {@code DataSource} object represents.
     *
     * @param username the database user on whose behalf the connection is
     *                 being made
     * @param password the user's password
     * @return a connection to the data source
     * @throws SQLException        if a database access error occurs
     * @throws SQLTimeoutException when the driver has determined that the
     *                             timeout value specified by the {@code setLoginTimeout} method
     *                             has been exceeded and has at least tried to cancel the
     *                             current database connection attempt
     * @since 1.4
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return doGetConnection(username, password);
    }

    private Connection doGetConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


}
