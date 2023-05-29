package org.jamie.security.demo.support.sms;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 短信验证码 Security配置类
 *              <br/> 存在一定问题 父抽象类为 SpringSecurity框架内部使用的类 需要需要做额外配置
 *              <br/> {@link  org.jamie.security.demo.config.SecurityConfig#addFilterAtSpecialOrder(HttpSecurity, Class, Integer)}
 * @date 2023/5/28 22:44
 */
public class SmsCodeConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractAuthenticationFilterConfigurer<H, SmsCodeConfigurer<H>, SmsCodeAuthenticationFilter> {

    public static final String DEFAULT_LOGIN_PROCESSING_URL = "/smsCodeLogin";
    public static final String DEFAULT_PHONE_PARAMETER = "mobilePhone";
    public static final String DEFAULT_CODE_PARAMETER = "smsCode";
    private String phoneParameter = DEFAULT_PHONE_PARAMETER;
    private String codeParameter = DEFAULT_CODE_PARAMETER;
    private SmsCodeAuthenticationFilter smsCodeAuthenticationFilter;
    /**
     * Creates a new instance with minimal defaults
     */
    public SmsCodeConfigurer() {
        super(new SmsCodeAuthenticationFilter(), DEFAULT_LOGIN_PROCESSING_URL);
        smsCodeAuthenticationFilter = super.getAuthenticationFilter();
    }

    public SmsCodeConfigurer(String loginProcessingUrl) {
        super(new SmsCodeAuthenticationFilter(), loginProcessingUrl);
        smsCodeAuthenticationFilter = super.getAuthenticationFilter();
    }

    /**
     * Create the {@link RequestMatcher} given a loginProcessingUrl
     *
     * @param loginProcessingUrl creates the {@link RequestMatcher} based upon the
     *                           loginProcessingUrl
     * @return the {@link RequestMatcher} to use based upon the loginProcessingUrl
     */
    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    @Override
    public void init(H http) throws Exception {
        super.init(http);
        smsCodeAuthenticationFilter.setPhoneParameter(this.phoneParameter);
        smsCodeAuthenticationFilter.setCodeParameter(this.codeParameter);
    }

    public SmsCodeConfigurer<H> phoneParameter(String phoneParameter) {
        this.phoneParameter = phoneParameter;
        return this;
    }

    public SmsCodeConfigurer<H> codeParameter(String codeParameter) {
        this.codeParameter = codeParameter;
        return this;
    }

}
