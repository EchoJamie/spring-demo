package com.jamie.pool.datasource;

import com.jamie.pool.proxy.ConnectionProxy;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jamie
 * @version 1.0.0
 * @description 池化连接 实现类
 * @date 2023/7/10 01:11
 */
public class PooledJamieDatasourceImpl extends AbstractJamieDatasourceImpl {

    private final List<ConnectionProxy> idleConnectionPool = new ArrayList<>();
    private final List<ConnectionProxy> activeConnectionPool = new ArrayList<>();

    /**
     * 最大使用连接数
     */
    private Integer poolMaxActiveSize = 10;
    /**
     * 最大空闲连接数
     */
    private Integer poolMaxIdleSize = 5;

    /**
     * 最大等待时间 毫秒
     */
    private Integer poolWaitingTime = 30000;

    /**
     * 获取连接 监视器对象
     */
    private final Object monitor = new Object();
    /**
     * 关闭连接 监视器对象
     */
    private final Object watch = new Object();

    /**
     * <p>Attempts to establish a connection with the data source that
     * this {@code DataSource} object represents.
     * <br/> 覆盖父类方法 返回代理连接
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
        ConnectionProxy proxyConn = getProxyConn(super.getUsername(), super.getPassword());
        return proxyConn.getProxyConnection();
    }

    private ConnectionProxy getProxyConn(String username, String password) throws SQLException {
        boolean wait = false;
        ConnectionProxy proxy = null;
        while (proxy == null) {
            synchronized (monitor) {
                // 空闲连接池 不为空，直接获取连接
                if (!idleConnectionPool.isEmpty()) {
                    proxy = idleConnectionPool.remove(0);
                } else {
                    // 没有空闲连接可以使用 创建新的连接（判断是否大于最大连接数）
                    if (activeConnectionPool.size() < poolMaxActiveSize) {
                        proxy = new ConnectionProxy(super.getConnection(), this);
                    }
                }
            }

            if (!wait) {
                wait = true;
            }

            if (proxy == null) {
                try {
                    monitor.wait(poolWaitingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 等待被打断
                    break;
                }
            }
        }

        if (proxy != null) {
            activeConnectionPool.add(proxy);
        }
        return proxy;
    }

    public void closeConnection(ConnectionProxy proxy) {
        synchronized (monitor) {
            activeConnectionPool.remove(proxy);

            if (idleConnectionPool.size() < poolMaxIdleSize) {
                idleConnectionPool.add(proxy);
            }

            // 唤醒 等待拿去连接的线程
            monitor.notify();
        }
    }
}
