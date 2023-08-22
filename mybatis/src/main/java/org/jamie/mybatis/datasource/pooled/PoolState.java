package org.jamie.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 池状态
 * @date 2023/02/12 18:15
 */
public class PoolState {

    protected PooledDataSource dataSource;

    /**
     * 空闲的 池化连接
     */
    protected final List<PooledConnection> idleConnections = new ArrayList<>();
    /**
     * 激活的 池化连接
     */
    protected final List<PooledConnection> activeConnections = new ArrayList<>();

    /**
     * 请求数
     */
    protected long requestCount = 0;
    /**
     * 累计请求次数
     */
    protected long accumulatedRequestTime = 0;
    /**
     * 累计检查此时
     */
    protected long accumulatedCheckoutTime = 0;
    /**
     * 所有已经 过期 的 连接数
     */
    protected long claimedOverdueConnectionCount = 0;
    /**
     * 过期连接的 累计检查
     */
    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;
    /**
     * 累计等待时间
     */
    protected long accumulatedWaitTime = 0;
    /**
     * 不得不等待次数
     */
    protected long hadToWaitCount = 0;
    /**
     * 损坏连接数
     */
    protected long badConnectionCount = 0;

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return 请求数
     */
    public synchronized long getRequestCount() {
        return requestCount;
    }

    /**
     * @return 平均请求时间
     */
    public synchronized long getAverageRequestTime() {
        return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
    }

    /**
     * @return 平均等待时间
     */
    public synchronized long getAverageWaitTime() {
        return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;
    }

    /**
     * @return 必须等待次数
     */
    public synchronized long getHadToWaitCount() {
        return hadToWaitCount;
    }

    /**
     * @return 坏 连接数
     */
    public synchronized long getBadConnectionCount() {
        return badConnectionCount;
    }

    /**
     * @return 所有已经 过期 的 连接数
     */
    public synchronized long getClaimedOverdueConnectionCount() {
        return claimedOverdueConnectionCount;
    }

    /**
     * @return 平均过期检查次数
     */
    public synchronized long getAverageOverdueCheckoutTime() {
        return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
    }

    /**
     * @return 平均检查次数
     */
    public synchronized long getAverageCheckoutTime() {
        return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
    }

    /**
     * @return 空闲连接数
     */
    public synchronized int getIdleConnectionCount() {
        return idleConnections.size();
    }

    /**
     * @return 激活连接数
     */
    public synchronized int getActiveConnectionCount() {
        return activeConnections.size();
    }

    @Override
    public synchronized String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n===CONFINGURATION==============================================");
        builder.append("\n jdbcDriver                     ").append(dataSource.getDriver());
        builder.append("\n jdbcUrl                        ").append(dataSource.getUrl());
        builder.append("\n jdbcUsername                   ").append(dataSource.getUsername());
        builder.append("\n jdbcPassword                   ").append(dataSource.getPassword() == null ? "NULL" : "************");
        builder.append("\n poolMaxActiveConnections       ").append(dataSource.poolMaximumActiveConnections);
        builder.append("\n poolMaxIdleConnections         ").append(dataSource.poolMaximumIdleConnections);
        builder.append("\n poolMaxCheckoutTime            ").append(dataSource.poolMaximumCheckoutTime);
        builder.append("\n poolTimeToWait                 ").append(dataSource.poolTimeToWait);
        builder.append("\n poolPingEnabled                ").append(dataSource.poolPingEnabled);
        builder.append("\n poolPingQuery                  ").append(dataSource.poolPingQuery);
        builder.append("\n poolPingConnectionsNotUsedFor  ").append(dataSource.poolPingConnectionsNotUsedFor);
        builder.append("\n ---STATUS-----------------------------------------------------");
        builder.append("\n activeConnections              ").append(getActiveConnectionCount());
        builder.append("\n idleConnections                ").append(getIdleConnectionCount());
        builder.append("\n requestCount                   ").append(getRequestCount());
        builder.append("\n averageRequestTime             ").append(getAverageRequestTime());
        builder.append("\n averageCheckoutTime            ").append(getAverageCheckoutTime());
        builder.append("\n claimedOverdue                 ").append(getClaimedOverdueConnectionCount());
        builder.append("\n averageOverdueCheckoutTime     ").append(getAverageOverdueCheckoutTime());
        builder.append("\n hadToWait                      ").append(getHadToWaitCount());
        builder.append("\n averageWaitTime                ").append(getAverageWaitTime());
        builder.append("\n badConnectionCount             ").append(getBadConnectionCount());
        builder.append("\n===============================================================");
        return builder.toString();
    }
}
