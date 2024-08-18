package org.jamie.spring.context;

import java.util.EventListener;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 09:46
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    void onApplicationEvent(E event);
}
