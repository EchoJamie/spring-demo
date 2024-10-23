package org.jamie.rpc.spi;

import lombok.extern.slf4j.Slf4j;
import org.jamie.rpc.serializer.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI 加载器
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 11:01
 */
@Slf4j
public class SpiLoader {

    /**
     * SPI 缓存
     */
    protected static final Map<Class<?>, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * SPI 实例缓存
     */
    protected static final Map<String, Class<?>> instanceCache = new ConcurrentHashMap<>();

    /**
     * SPI 配置文件路径
     */
    protected static final String SPI_PATH = "META-INF/rpc";

    /**
     * 系统 SPI 配置文件路径
     */
    public static final String SYSTEM_SPI_PATH = SPI_PATH + "/system";

    /**
     * 自定义 SPI 配置文件路径
     */
    public static final String CUSTOM_SPI_PATH = SPI_PATH + "/custom";

    protected static final String[] scanPaths = new String[]{SYSTEM_SPI_PATH, CUSTOM_SPI_PATH};

    protected static final List<Class<?>> LOADER_CLASS_LIST = Arrays.asList(Serializer.class);

    public static <T> T getInstance(Class<T> interfaceClass, String name) {

        try {
            @SuppressWarnings("unchecked")
            T t = (T) loaderMap.get(interfaceClass).get(name).newInstance();
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadAll() throws IOException {
        // 基础加载
        for (Class<?> scanPath : LOADER_CLASS_LIST) {
            load(scanPath);
        }
    }

    public static Map<String, Class<?>> load(Class<?> clazz) throws IOException {
        log.info("load interface {}", clazz.getName());
        // 扫描路径，用户自定义的 SPI 优先级高于系统 SPI
        Map<String, Class<?>> keyClassMap = new HashMap<>();

        // read spi config file
        ClassLoader cl = SpiLoader.class.getClassLoader();

        for (String scanPath : scanPaths) {
            @SuppressWarnings("unchecked")
            Enumeration<URL> iter = cl.getResources(scanPath + "/" + clazz.getName());
            while (iter.hasMoreElements()) {
                URL url = iter.nextElement();
                try {
                    log.info("load spi config file {}", url.getPath());
                    InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] split = line.split("=");
                        String name = split[0];
                        String className = split[1];
                        // load
                        Class<?> cls = Class.forName(className);
                        log.info("load spi {} {}", name, cls.getName());
                        keyClassMap.put(name, cls);
                    }
                } catch (Exception e) {
                    log.error("load spi config file {} error", url.getPath(), e);
                }
            }
        }
        // cache but no instance
        loaderMap.put(clazz, keyClassMap);
        return keyClassMap;
    }

    public static void main(String[] args) throws IOException {
        loadAll();
        loaderMap.forEach((k, v) -> {
            log.info("{} size() -> {}", k.getTypeName(), v.size());
            v.forEach((vk, vv) -> log.info("{} \t: {}", vk, vv.getTypeName()));
        });
    }
}
