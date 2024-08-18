package org.jamie.spring.context;

import org.jamie.spring.bean.HierarchicalBeanFactory;
import org.jamie.spring.bean.ListableBeanFactory;
import org.jamie.spring.resource.ResourceLoader;

/**
 * @author jamie
 * @version 1.0.0
 * @description 应用上下文
 * @date 2022/12/29 22:38
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
