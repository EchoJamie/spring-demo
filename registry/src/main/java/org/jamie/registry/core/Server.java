package org.jamie.registry.core;

import org.jamie.registry.Lifecycle;

/**
 * 服务端接口
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/22 19:25
 */
public interface Server extends Lifecycle {

    int DEFAULT_PORT = 8741;

    /**
     *
     */
    @Override
    default void start() {
        start(DEFAULT_PORT);
    }

    /**
     * 启动
     */
    void start(int port);

}
