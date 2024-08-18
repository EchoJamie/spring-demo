package org.jamie.spring.context.event;

import org.jamie.spring.context.ApplicationContext;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 23:24
 */
public class ContextClosedEvent extends ApplicationContextEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }
}
