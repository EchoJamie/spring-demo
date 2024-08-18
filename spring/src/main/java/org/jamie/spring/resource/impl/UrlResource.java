package org.jamie.spring.resource.impl;

import org.jamie.spring.resource.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 对java.net.URL进行资源定位的实现类
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:31
 */
public class UrlResource implements Resource {

    private final URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        try {
            return con.getInputStream();
        } catch (IOException ex) {
            throw ex;
        }
    }
}
