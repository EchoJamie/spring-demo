package org.jamie.security.demo.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 用户查询服务
 * @date 2023/5/28 16:42
 */
@Component
public class FrameWorkUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, "123312231123", Collections.emptyList());
    }
}
