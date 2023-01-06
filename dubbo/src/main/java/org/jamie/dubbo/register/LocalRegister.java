package org.jamie.dubbo.register;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jamie
 * @version 1.0.0
 * @description 服务本地注册
 * @date 2023/01/02 12:33
 */
public class LocalRegister {

    private static Map<String, Class> map = new HashMap<>();

    public static void register(String interfaceName, String version, Class implClass) {
        map.put(interfaceName + ":" + version, implClass);
    }

    public static Class get(String interfaceName, String version) {
        return map.get(interfaceName + ":" + version);
    }

}
