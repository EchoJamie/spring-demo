package org.jamie.spring.context;

import org.jamie.spring.bean.Aware;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 09:25
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);
}
