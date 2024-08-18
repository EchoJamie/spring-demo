package org.jamie.spring.resource;

/**
 * 资源加载器
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:30
 */
public interface ResourceLoader {

    Resource getResource(String location);

}
