package org.jamie.mybatis.io;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 资源工具
 * @date 2023/02/11 16:36
 */
public class Resources {

    private static Charset charset;

    private static ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return classLoaderWrapper.classForName(className);
    }

    public static Reader getResourceAsReader(String resourceName) {
        Reader reader = null;
        if (charset == null) {
            reader = new InputStreamReader(getResourceAsStream(resourceName));
        } else {
            reader = new InputStreamReader(getResourceAsStream(resourceName), charset);
        }
        return reader;
    }

    public static InputStream getResourceAsStream(String resourceName) {
        return classLoaderWrapper.getResourceAsStream(resourceName, null);
    }
}
