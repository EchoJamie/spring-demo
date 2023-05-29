package org.jamie.security.demo.support.sms;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 短信验证码 认证过滤器
 * @date 2023/5/28 22:22
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String DEFAULT_LOGIN_PROCESSING_URL = "/smsCodeLogin";
    private String phoneParameter;
    private String codeParameter;

    public SmsCodeAuthenticationFilter() {
        this(DEFAULT_LOGIN_PROCESSING_URL);
    }

    /**
     * @param defaultFilterProcessesUrl the default value for <tt>filterProcessesUrl</tt>.
     */
    protected SmsCodeAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    /**
     * Performs actual authentication.
     * <p>
     * The implementation should do one of the following:
     * <ol>
     * <li>Return a populated authentication token for the authenticated user, indicating
     * successful authentication</li>
     * <li>Return null, indicating that the authentication process is still in progress.
     * Before returning, the implementation should perform any additional work required to
     * complete the process.</li>
     * <li>Throw an <tt>AuthenticationException</tt> if the authentication process
     * fails</li>
     * </ol>
     *
     * @param request  from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     *                 redirect as part of a multi-stage authentication process (such as OpenID).
     * @return the authenticated user token, or null if authentication is incomplete.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 获取参数
        String mobilePhone = obtainPhone(request);
        mobilePhone = (mobilePhone != null) ? mobilePhone.trim() : "";
        String smsCode = obtainCode(request);
        smsCode = (smsCode != null) ? smsCode.trim() : "";


        // 构建认证对象
        SmsCodeAuthenticationToken authRequest = SmsCodeAuthenticationToken.unauthenticated(mobilePhone, smsCode);
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

        // 返回认证对象
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    public void setPhoneParameter(String phoneParameter) {
        this.phoneParameter = phoneParameter;
    }

    public void setCodeParameter(String codeParameter) {
        this.codeParameter = codeParameter;
    }

    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter(this.phoneParameter);
    }

    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(this.codeParameter);
    }
}
