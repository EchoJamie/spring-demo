package org.jamie.mybatis.datasource.pooled;

import org.jamie.mybatis.datasource.unpooled.UnpooledDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 池化数据源 【需要补充各个配置项的 Getter 和 Setter 方法】
 * @date 2023/02/11 15:44
 */
public class PooledDataSource implements DataSource {

    /**
     * 连接池状态
     */
    private final PoolState poolState = new PoolState(this);
    private final UnpooledDataSource dataSource;

    // OPTIONAL CONFIGURATION FIELDS
    // 可选的 配置 字段
    protected int poolMaximumActiveConnections = 10;
    protected int poolMaximumIdleConnections = 5;
    protected int poolMaximumCheckoutTime = 20000;
    protected int poolTimeToWait = 20000;
    protected int poolMaximumLocalBadConnectionTolerance = 3;
    /**
     * 连通性 检查 SQL
     */
    protected String poolPingQuery = "NO PING QUERY SET";
    /**
     * 连通性 检查 开关
     */
    protected boolean poolPingEnabled;
    /**
     * 连通性 检查 的 检查时间间隔
     */
    protected int poolPingConnectionsNotUsedFor;
    private int expectedConnectionTypeCode;

    // 构造器
    public PooledDataSource() {
        dataSource = new UnpooledDataSource();
    }

    public PooledDataSource(UnpooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public PooledDataSource(String driver, String url, String username, String password) {
        dataSource = new UnpooledDataSource(driver, url, username, password);
        expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
    }

    public PooledDataSource(String driver, String url, Properties driverProperties) {
        dataSource = new UnpooledDataSource(driver, url, driverProperties);
        expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
    }

    public PooledDataSource(ClassLoader driverClassLoader, String driver, String url, String username, String password) {
        dataSource = new UnpooledDataSource(driverClassLoader, driver, url, username, password);
        expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
    }

    public PooledDataSource(ClassLoader driverClassLoader, String driver, String url, Properties driverProperties) {
        dataSource = new UnpooledDataSource(driverClassLoader, driver, url, driverProperties);
        expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
    }

    /**
     * 组装连接 类型编码
     *
     * @param url
     * @param username
     * @param password
     * @return
     */
    private int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password).getProxyConnection();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int loginTimeout) throws SQLException {
        DriverManager.setLoginTimeout(loginTimeout);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }


    public String getDriver() {
        return this.dataSource.getDriver();
    }

    public String getUrl() {
        return this.dataSource.getUrl();
    }

    public String getUsername() {
        return this.dataSource.getUsername();
    }

    public String getPassword() {
        return this.dataSource.getPassword();
    }

    /**
     * 获取连接
     *
     * @param username
     * @param password
     * @return
     */
    private PooledConnection popConnection(String username, String password) throws SQLException {
        boolean countedWait = false;
        PooledConnection conn = null;
        // 开始获取连接的 时间
        long t = System.currentTimeMillis();
        // 记录 当前获取连接时 获取坏连接后 重试次数
        int localBadConnectionCount = 0;

        // 同步代码块
        // 锁标识：连接池状态
        while (conn == null) {
            synchronized (poolState) {
                // 空闲连接队列 不为空
                if (!poolState.idleConnections.isEmpty()) {
                    // 从空闲连接队列中 获取连接
                    conn = poolState.idleConnections.remove(0);
                    System.out.println("Checked out connection " + conn.getRealHashCode() + " from pool.");
                } else {
                    // 检查 最大连接数
                    if (poolState.activeConnections.size() < poolMaximumActiveConnections) {
                        // 创建新连接
                        conn = new PooledConnection(dataSource.getConnection(), this);
                        System.out.println("Created connection " + conn.getRealHashCode() + ".");
                    } else {
                        // 无法创建连接
                        // 获取 最旧的 连接
                        PooledConnection oldestActiveConnection = poolState.activeConnections.get(0);
                        // 获取 最久的连接 的 被获取次数
                        long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                        // 判断连接 是否 超过最大使用次数
                        if (longestCheckoutTime > poolMaximumCheckoutTime) {
                            // Can claim overdue connection
                            poolState.claimedOverdueConnectionCount++;
                            poolState.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            poolState.accumulatedCheckoutTime += longestCheckoutTime;
                            // 移除连接
                            poolState.activeConnections.remove(oldestActiveConnection);
                            // 连接是否 开启自动提交
                            if (!oldestActiveConnection.getRealConnection().getAutoCommit()) {
                                // 未开启自动提交
                                // 回滚 未提交 【清理未提交区】
                                try {
                                    oldestActiveConnection.getRealConnection().rollback();
                                } catch (SQLException e) {
                                    /*
                                     Just log a message for debug and continue to execute the following
                                     statement like nothing happened.
                                     Wrap the bad connection with a new PooledConnection, this will help
                                     to not interrupt current executing thread and give current thread a
                                     chance to join the next competition for another valid/good database
                                     connection. At the end of this loop, bad {@link @conn} will be set as null.
                                    */
                                    System.out.println("Bad connection. Could not roll back");
                                }
                                // 创建新连接
                                conn = new PooledConnection(oldestActiveConnection.getRealConnection(), this);
                                conn.setCreatedTimestamp(oldestActiveConnection.getCreatedTimestamp());
                                conn.setLastUsedTimestamp(oldestActiveConnection.getLastUsedTimestamp());
                                oldestActiveConnection.invalidate();
                                System.out.println("Claimed overdue connection " + conn.getRealHashCode() + ".");
                            } else {
                                // Must wait
                                // 等待 连接 (等待 poolState.notifyAll() 的调用)
                                try {
                                    if (!countedWait) {
                                        // 强制 等待
                                        poolState.hadToWaitCount++;
                                        countedWait = true;
                                    }
                                    // TODO 日志 打印等待时长
                                    System.out.println("Waiting as long as " + poolTimeToWait + " milliseconds for connection.");
                                    long wt = System.currentTimeMillis();
                                    poolState.wait(poolTimeToWait);
                                    // 记录累计 等待时间
                                    poolState.accumulatedWaitTime += System.currentTimeMillis() - wt;
                                } catch (InterruptedException e) {
                                    break;
                                }
                            }
                        }
                    }
                }

                // 获取到了连接
                if (conn != null) {
                    // ping to server and check the connection is valid or not
                    // 检查连接是否可用
                    if (conn.isValid()) {
                        // 连接可用
                        // 回滚 未提交 【清理未提交区】
                        if (!conn.getRealConnection().getAutoCommit()) {
                            conn.getRealConnection().rollback();
                        }
                        // 重置 部分属性
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), username, password));
                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        // 将当前连接记录到 活动连接队列中
                        poolState.activeConnections.add(conn);
                        poolState.requestCount++;
                        poolState.accumulatedRequestTime += System.currentTimeMillis() - t;
                    } else {
                        // 连接不可用
                        // TODO 日志 重新获取连接
                        System.out.println("A bad connection (" + conn.getRealHashCode() + ") was returned from the pool, getting another connection.");
                        poolState.badConnectionCount++;
                        // 记录 当前重试次数
                        localBadConnectionCount++;
                        // 连接重置为 空 , 继续循环获取
                        conn = null;
                        if (localBadConnectionCount > (poolMaximumIdleConnections + poolMaximumLocalBadConnectionTolerance)) {
                            // TODO 日志
                            // 当前错误连接数 超出 最大空闲连接数与 最大容忍坏连接数
                            System.out.println("PooledDataSource: Could not get a good connection to the database.");
                            throw new SQLException("PooledDataSource: Could not get a good connection to the database.");
                        }
                    }
                }
            }
        }

        // 获取到不可用连接 并且 当前错误连接数 超出 最大空闲连接数与 最大容忍坏连接数
        // 或
        // 唤醒时 出现 InterruptedException 中断异常
        if (conn == null) {
            System.out.println("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
            throw new SQLException("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
        }

        return conn;
    }

    /**
     * 测试连接 连通性
     * <ol>
     *  <li> 连接是否关闭；</li>
     *  <li> 连通性检查开关 是否开启; </li>
     *  <li> 连接是否需要 进行连通性检查;（ 上次使用时间到现在的 时间间隔 是否 超出 配置的 需要检查连通性的 时间值 ）</li>
     * </ol>
     *
     * @param pooledConnection
     * @return
     */
    public boolean pingConnection(PooledConnection pooledConnection) {
        boolean result = false;

        try {
            result = !pooledConnection.getRealConnection().isClosed();
        } catch (SQLException e) {
            // TODO 日志
            System.out.println("Connection " + pooledConnection.getRealHashCode() + " is BAD: " + e.getMessage());
            result = false;
        }

        // 连接未关闭
        if (result) {
            // 连通性 检查 是否启用
            if (poolPingEnabled) {
                // 判断是否配置了 检查的连接的检查时间间隔 以及最后连接时间 是否超出时间间隔
                // 未超出  则认为连接可用
                // 超出了  则认为需要进行连通性检查
                if (poolPingConnectionsNotUsedFor >= 0 && pooledConnection.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor) {
                    try {
                        // TODO 日志 检查开始
                        System.out.println("Testing connection " + pooledConnection.getRealHashCode() + " ...");
                        Connection realConnection = pooledConnection.getRealConnection();
                        try (Statement statement = realConnection.createStatement()) {
                            statement.executeQuery(poolPingQuery).close();
                        }
                        // 未开启自动提交 则 回滚
                        if (!realConnection.getAutoCommit()) {
                            realConnection.rollback();
                        }
                        result = true;
                        // TODO 日志 连接正常
                        System.out.println("Connection " + pooledConnection.getRealHashCode() + " is GOOD!");

                    } catch (SQLException e) {
                        // TODO 日志 【警告】执行检查SQL 失败
                        System.out.println("Execution of ping query '" + poolPingQuery + "' failed: " + e.getMessage());
                        // 关闭连接
                        try {
                            pooledConnection.getRealConnection().close();
                        } catch (Exception e2) {
                            //ignore
                        }
                        result = false;
                        // TODO 日志
                        System.out.println("Connection " + pooledConnection.getRealHashCode() + " is BAD: " + e.getMessage());
                    }
                }
            }
        }
        return result;
    }

    /**
     * 归还连接
     *
     * @param pooledConnection
     * @throws SQLException
     */
    public void pushConnection(PooledConnection pooledConnection) throws SQLException {
        // 同步代码块
        // 锁标识：连接池状态
        synchronized (poolState) {
            // 激活的 连接中 移除对应连接
            poolState.activeConnections.remove(pooledConnection);
            // 实际连接 不为空 且 通过了 连通性测试
            if (pooledConnection.isValid()) {
                // 空闲连接数 小于 配置的 最大空闲连接数 并且 连接类型Code 与 预期连接类型编码相同
                if (poolState.idleConnections.size() < poolMaximumIdleConnections && pooledConnection.getConnectionTypeCode() == expectedConnectionTypeCode) {
                    // 记录累计 获取时间
                    poolState.accumulatedCheckoutTime += pooledConnection.getCheckoutTime();
                    // 回滚 未提交 【清理未提交区】
                    if (!pooledConnection.getRealConnection().getAutoCommit()) {
                        pooledConnection.getRealConnection().rollback();
                    }
                    // 使用实际连接 构建 新的池化连接
                    PooledConnection newPooledConnection = new PooledConnection(pooledConnection.getRealConnection(), this);
                    // 添加到 空闲连接队列中
                    poolState.idleConnections.add(newPooledConnection);
                    // 配置属性
                    newPooledConnection.setCreatedTimestamp(pooledConnection.getCreatedTimestamp());
                    newPooledConnection.setLastUsedTimestamp(pooledConnection.getLastUsedTimestamp());
                    // 禁用原来的连接
                    pooledConnection.invalidate();
                    // TODO 日志 归还连接 到池中
                    System.out.println("Returned connection " + newPooledConnection.getRealHashCode() + " to pool.");
                    poolState.notifyAll();
                } else {
                    // 空闲连接队列已满 或 连接类型 不符合预期
                    // 记录累计 获取时间
                    poolState.accumulatedCheckoutTime += pooledConnection.getCheckoutTime();
                    // 回滚 未提交 【清理未提交区】
                    if (!pooledConnection.getRealConnection().getAutoCommit()) {
                        pooledConnection.getRealConnection().rollback();
                    }
                    // 关闭连接
                    pooledConnection.getRealConnection().close();
                    System.out.println("Closed connection " + pooledConnection.getRealHashCode() + ".");
                    // 销毁 池化连接
                    pooledConnection.invalidate();
                }
            } else {
                // 连接不可用
                // 一个 损坏的 连接尝试 归还到 连接池中, 已销毁.
                System.out.println("A bad connection (" + pooledConnection.getRealHashCode() + ") attempted to return to the pool, discarding connection.");
                // 记录损坏连接数
                poolState.badConnectionCount++;
            }
        }
    }
}
