package org.jamie.security.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.jamie.security.demo.util.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 认证失败处理器
 * @date 2023/5/28 16:11
 */
@Slf4j
@Component
public class FrameworkAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("登录失败: {}", exception.getMessage());
        ResponseUtils.responseJson(response, ResponseUtils.ResponseVO.failure(403, "登录失败"));
    }
}
