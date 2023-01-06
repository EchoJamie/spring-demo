package org.jamie.dubbo.register;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jamie
 * @version 1.0.0
 * @description 服务发现 服务远程注册
 * @date 2023/01/03 15:19
 */
public class RemoteServiceRegister {

    private static Map<String, List<URL>> REGISTER = new HashMap<>();

    private static final String FILE_PATH = "/Users/jamie/namespace/spring-demo/dubbo/src/main/resources/temp.txt";

    public static void register(String serviceName, String tag, URL url) {
        List<URL> urls = REGISTER.get(serviceName);
        if (urls == null) {
            urls = new ArrayList<>();
        }
        urls.add(url);
        REGISTER.put(serviceName + ":" + tag, urls);
        // 文件替代nacos zookeeper redis
        write2File(REGISTER);
        System.out.println(REGISTER);
    }

    public static List<URL> get(String serviceName, String tag) {
        // 文件替代nacos zookeeper redis
        REGISTER = getFromFile();
        System.out.println(REGISTER);
        List<URL> urls = REGISTER.get(serviceName + ":" + tag);
        return urls;
    }

    private static void write2File(Map<String, List<URL>> register) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
            oos.writeObject(register);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, List<URL>> getFromFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            Map<String, List<URL>> register = (Map<String, List<URL>>) ois.readObject();
            ois.close();
            return register;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
