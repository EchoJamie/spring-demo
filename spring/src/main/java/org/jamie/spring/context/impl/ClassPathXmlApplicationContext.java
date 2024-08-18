package org.jamie.spring.context.impl;

import org.jamie.spring.context.support.AbstractXmlApplicationContext;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 09:31
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private final String[] configLocations;

    public ClassPathXmlApplicationContext(String configLocation) {
        this(new String[]{configLocation});
    }

    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }

}
