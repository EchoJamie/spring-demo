package org.jamie.registry;

/**
 * 生命周期
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/22 19:19
 */
public interface Lifecycle {

    /**
     * 初始化
     */
    default void init() {
    }


    /**
     * 启动
     */
    void start();
}
