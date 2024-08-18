package org.jamie.spring.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 资源类
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:29
 */
public interface Resource {
    InputStream getInputStream() throws IOException;
}
