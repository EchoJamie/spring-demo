package org.jamie.spring.context.event;

import org.jamie.spring.context.ApplicationContext;
import org.jamie.spring.context.ApplicationEvent;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 23:13
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }

}
