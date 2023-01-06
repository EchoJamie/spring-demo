package org.jamie.dubbo.protocol.http;

import org.apache.commons.io.IOUtils;
import org.jamie.dubbo.protocol.Invocation;
import org.jamie.dubbo.register.LocalRegister;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jamie
 * @version 1.0.0
 * @description 请求路由分发处理器
 * @date 2023/01/02 12:12
 */
public class HttpServerHandler {

    public void handle(ServletRequest req, ServletResponse resp) throws IOException {
        // 处理请求的逻辑
        try {

            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();

            Class implClass = LocalRegister.get(invocation.getInterfaceName(), invocation.getVersion());

            Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());

            String result = (String) method.invoke(implClass.newInstance(), invocation.getParams());

            IOUtils.write(result, resp.getOutputStream());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
