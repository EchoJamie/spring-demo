package org.jamie.spring.context;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 09:46
 */
public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);

    void removeApplicationListener(ApplicationListener<?> listener);

    void multicastEvent(ApplicationEvent event);
}
