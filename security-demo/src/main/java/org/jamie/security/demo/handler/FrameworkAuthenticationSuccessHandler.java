package org.jamie.security.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.jamie.security.demo.util.ResponseUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 认证成功处理器
 * @date 2023/5/28 16:11
 */
@Slf4j
@Component
public class FrameworkAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("{} 登录成功了", authentication.getPrincipal());
        ResponseUtils.responseJson(response, ResponseUtils.ResponseVO.success("登录成功"));
    }
}
