package org.jamie.dubbo.protocol;

import org.jamie.dubbo.register.URL;

import java.util.List;
import java.util.Random;

/**
 * @author jamie
 * @version 1.0.0
 * @description 负载均衡器
 * @date 2023/01/03 16:05
 */
public class LoadBalance {

    public static URL random(List<URL> urls) {
        Random random = new Random();
        int n = random.nextInt(urls.size());
        URL url = urls.get(n);
//        int n = random.nextInt(5);
//        System.out.println("random: " + n);
//        URL url = urls.get(0);
        System.out.println(url);
        return url;
    }
}
