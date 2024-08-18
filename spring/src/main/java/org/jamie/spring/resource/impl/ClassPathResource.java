package org.jamie.spring.resource.impl;

import org.jamie.spring.resource.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * classpath下资源的实现类
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:31
 */
public class ClassPathResource implements Resource {

    private final String path;

    public ClassPathResource(String path) {
        this.path = path;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.path);
        if (is == null) {
            throw new FileNotFoundException(this.path + " cannot be opened because it does not exist");
        }
        return is;
    }
}
