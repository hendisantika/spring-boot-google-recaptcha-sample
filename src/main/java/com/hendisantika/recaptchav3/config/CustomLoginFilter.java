package com.hendisantika.recaptchav3.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

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

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String recaptchaFormResponse = request.getParameter("g-recaptcha-response");

        log.info("Before processing authentication.......");

        ReCaptchaV3Handler handler = new ReCaptchaV3Handler();

        try {
            float score = handler.verify(recaptchaFormResponse);
            if (score < 0.5) {
                request.getRequestDispatcher("otp_login").forward(request, response);
            }
        } catch (InvalidReCaptchaTokenException | ServletException | IOException e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        return super.attemptAuthentication(request, response);
    }

    private CustomLoginFilter getCustomLoginFilter() throws Exception {
        CustomLoginFilter filter = new CustomLoginFilter("/login", "POST");
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/processLogin");
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                if (!response.isCommitted()) {
                    response.sendRedirect("/success");
                }
            }
        });
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                if (!response.isCommitted()) {
                    response.sendRedirect("login?error");
                }
            }
        });

        return filter;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(getCustomLoginFilter(), CustomLoginFilter.class);
        return http.build();
    }
}
