package org.jamie.security.demo.config;

import lombok.SneakyThrows;
import org.jamie.security.demo.support.sms.SmsCodeAuthenticationFilter;
import org.jamie.security.demo.support.sms.SmsCodeAuthenticationProvider;
import org.jamie.security.demo.support.sms.SmsCodeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.Filter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description spring security 配置
 * @date 2023/5/2 19:50
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {

        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationSuccessHandler authenticationSuccessHandler,
                                                   AuthenticationFailureHandler authenticationFailureHandler,
                                                   AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        // 表单登录配置
        http.formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                // 登录接口
                .loginProcessingUrl("/login")
                // 认证成功 处理器
                .successHandler(authenticationSuccessHandler)
                // 认证失败处理器
                .failureHandler(authenticationFailureHandler)
                .and()
                // 认证异常 处理器
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                // 认证请求 配置
                .authorizeRequests()
                // 匹配 放行
                .mvcMatchers("/index.html", "/").permitAll()
                // 匹配 拒绝
                .mvcMatchers("/5473").denyAll()
                // 任何请求 需要认证
                .anyRequest().authenticated()
                .and()
                // 跨域资源共享
                .cors().disable()
                // 跨站脚本攻击
                .csrf().disable();

        // UsernamePasswordAuthenticationProcessingFilter Order为 1900
        this.addFilterAtSpecialOrder(http, SmsCodeAuthenticationFilter.class, 1850);

        SmsCodeConfigurer<HttpSecurity> smsCodeConfigurer = new SmsCodeConfigurer<>();
        smsCodeConfigurer.loginProcessingUrl("/smsCodeLogin")
                .phoneParameter("mobilePhone")
                .codeParameter("smsCode")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

        http.apply(smsCodeConfigurer);
        http.authenticationProvider(new SmsCodeAuthenticationProvider());

        return http.build();
    }


    /**
     * 对于自定义实现的 AuthenticationProcessingFilter
     * <br/> 在 {@link org.springframework.security.config.annotation.web.builders.FilterOrderRegistration FilterOrderRegistration}中 SpringSecurity获取不到Order
     * <br/> 因此 通过反射方式 注入
     * @param httpSecurity
     * @param filterClazz
     * @param specialOrder 指定Order
     */
    @SneakyThrows
    private void addFilterAtSpecialOrder(HttpSecurity httpSecurity, Class<? extends Filter> filterClazz, Integer specialOrder) {
        Field filterOrders = HttpSecurity.class.getDeclaredField("filterOrders");
        boolean filterOrdersAccessible = filterOrders.isAccessible();
        filterOrders.setAccessible(true);
        Object filterOrderRegistration = filterOrders.get(httpSecurity);
        Method putMethod = filterOrderRegistration.getClass().getDeclaredMethod("put", Class.class, int.class);
        boolean putMethodAccessible = putMethod.isAccessible();
        putMethod.setAccessible(true);
        putMethod.invoke(filterOrderRegistration, filterClazz, specialOrder);
        putMethod.setAccessible(putMethodAccessible);
        filterOrders.setAccessible(filterOrdersAccessible);
    }
}
