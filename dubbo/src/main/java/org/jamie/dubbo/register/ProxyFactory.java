package org.jamie.dubbo.register;

import org.jamie.dubbo.protocol.Invocation;
import org.jamie.dubbo.protocol.LoadBalance;
import org.jamie.dubbo.protocol.http.HttpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author jamie
 * @version 1.0.0
 * @description 代理工厂
 * @date 2023/01/03 13:55
 */
public class ProxyFactory<T> {

    public static <T> T getProxy(final Class interfaceClass) {
        // 读取用户配置 使用用户配置的代理方式
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // mock 功能
                String mock = System.getProperty("mock");
                if (mock != null && mock.startsWith("return:")) {
                    String methodName = System.getProperty("method");
                    if (methodName == null) {
                        return mock.replace("return:", "");
                    } else if (methodName.contains(method.getName())) {
                        return mock.replace("return:", "");
                    }
                }

                // TODO 熔断逻辑 待补充

                // 核心逻辑
                HttpClient httpClient = new HttpClient();
                String version = "1.0";
                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(), method.getParameterTypes(), args, version);
                // 注册中心 获取URL列表
                List<URL> urls = RemoteServiceRegister.get(interfaceClass.getName(), version);

                String send;
                // 服务重试机制
                int count = 0;
                while (true) {
                    // try-catch 容错机制
                    try {
                        count++;
                        System.out.println("请求次数:" + count);
                        // 负载均衡 获取 请求地址
                        URL random = LoadBalance.random(urls);
                        send = httpClient.send(random.getIp(), random.getPort(), invocation);
                        return send;
                    } catch (Exception e) {
                        System.out.println("请求失败次数:" + count);
                        if (count < 3) {
                            continue;
                        }
                        send = "服务连接失败";
                        break;
                    }
                }
                return send;
            }
        });
    }
}
