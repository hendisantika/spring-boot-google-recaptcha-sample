package com.hendisantika.recaptchav3.config;

import com.hendisantika.recaptchav3.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-google-recaptcha-sample
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/11/23
 * Time: 17:30
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class AppConfig {
    private final UserService userService;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http, DaoAuthenticationProvider daoAuthenticationProvider) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        CustomLoginFilter customLoginFilter = new CustomLoginFilter("/processLogin", "POST");
        customLoginFilter.setAuthenticationManager(authenticationManager);
        customLoginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> response.sendRedirect("/success"));
        customLoginFilter.setAuthenticationFailureHandler((request, response, exception) -> response.sendRedirect("/login?error"));

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/otp_login").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/processLogin")
                        .defaultSuccessUrl("/success", true)
                        .permitAll())
                .addFilterBefore(customLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
