package org.jamie.security.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.jamie.security.demo.util.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 访问未认证资源时 响应结果
 * @date 2023/5/28 21:21
 */
@Slf4j
@Component
public class FrameworkAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseUtils.responseJson(response, ResponseUtils.ResponseVO.failure(403, "Forbidden"));
    }
}
