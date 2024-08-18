package org.jamie.spring.resource;

import org.jamie.spring.resource.impl.ClassPathResource;
import org.jamie.spring.resource.impl.FileSystemResource;
import org.jamie.spring.resource.impl.UrlResource;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:37
 */
public class DefaultResourceLoader implements ResourceLoader {

    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * @param location
     * @return
     */
    @Override
    public Resource getResource(String location) {
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            //classpath下的资源
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        } else {
            try {
                //尝试当成url来处理
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException ex) {
                //当成文件系统下的资源处理
                return new FileSystemResource(location);
            }
        }
    }
}
