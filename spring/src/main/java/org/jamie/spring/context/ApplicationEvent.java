package org.jamie.spring.context;

import java.util.EventObject;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 01:01
 */
public abstract class ApplicationEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
