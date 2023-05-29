package org.jamie.security.demo.support.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 短信验证码 认证提供者
 * @date 2023/5/28 22:29
 */
@Slf4j
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {


    /**
     * Performs authentication with the same contract as
     * {@link AuthenticationManager#authenticate(Authentication)}
     * .
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return
     * <code>null</code> if the <code>AuthenticationProvider</code> is unable to support
     * authentication of the passed <code>Authentication</code> object. In such a case,
     * the next <code>AuthenticationProvider</code> that supports the presented
     * <code>Authentication</code> class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String mobilePhone = (String) authentication.getPrincipal();
        String smsCode = (String) authentication.getCredentials();
        log.info("手机号【{}】验证码【{}】开始登录...", mobilePhone, smsCode);
        // 1. 验证码 从存储介质中拿出来

        // 2. 根据手机号拿
        if (!StringUtils.hasText(mobilePhone)) {
            throw new UsernameNotFoundException("手机号不可为空");
        }

        // 3. 对比验证码是否匹配
        if (smsCode.equals("9473")) {
            // 4. 一致 认证成功
            return new SmsCodeAuthenticationToken(mobilePhone, smsCode, Collections.emptyList());
        } else {
            // 5. 不一致 认证失败 抛出异常 BadCredentialsException
            throw new BadCredentialsException("验证码错误");
        }
    }

    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code> supports the
     * indicated <Code>Authentication</code> object.
     * <p>
     * Returning <code>true</code> does not guarantee an
     * <code>AuthenticationProvider</code> will be able to authenticate the presented
     * instance of the <code>Authentication</code> class. It simply indicates it can
     * support closer evaluation of it. An <code>AuthenticationProvider</code> can still
     * return <code>null</code> from the {@link #authenticate(Authentication)} method to
     * indicate another <code>AuthenticationProvider</code> should be tried.
     * </p>
     * <p>
     * Selection of an <code>AuthenticationProvider</code> capable of performing
     * authentication is conducted at runtime the <code>ProviderManager</code>.
     * </p>
     *
     * @param authentication
     * @return <code>true</code> if the implementation can more closely evaluate the
     * <code>Authentication</code> class presented
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsCodeAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
