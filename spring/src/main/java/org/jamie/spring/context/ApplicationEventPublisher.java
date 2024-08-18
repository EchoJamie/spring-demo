package org.jamie.spring.context;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 01:00
 */
public interface ApplicationEventPublisher {

    /**
     * 发布事件
     *
     * @param event
     */
    void publishEvent(ApplicationEvent event);
}
