package com.hendisantika.recaptchav3.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-google-recaptcha-sample
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/11/23
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    public CustomLoginFilter(String loginURL, String httpMethod) {
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(loginURL, httpMethod));
    }


}
