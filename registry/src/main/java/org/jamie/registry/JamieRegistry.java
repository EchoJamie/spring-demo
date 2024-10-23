package org.jamie.registry;

/**
 * @author jamie
 * @version 1.0.0
 * @description 注册中心
 * @date 2023/9/15 19:02
 */
public interface JamieRegistry {

    public static final int PORT = 8741;

    /**
     * 注册节点
     *
     * @return
     */
    boolean register();

    /**
     * 注销节点
     *
     * @return
     */
    boolean unregister();

    /**
     * 心跳检测
     */
    void heartBeatCheck();

}
