package org.jamie.dubbo.protocol.http;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * @author jamie
 * @version 1.0.0
 * @description 请求分发 Servlet
 * @date 2023/01/02 12:11
 */
public class DispatcherServlet extends HttpServlet {

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        // 读取配置
        new HttpServerHandler().handle(req, resp);
    }
}
