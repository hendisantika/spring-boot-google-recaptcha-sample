package com.hendisantika.recaptchav3.service;

import com.hendisantika.recaptchav3.entity.User;
import com.hendisantika.recaptchav3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-google-recaptcha-sample
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/11/23
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepository.findAppUserByUserName(username);
        if (username == null) {
            throw new UsernameNotFoundException("user not registered");
        } else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            appUser.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            });
            return new org.springframework.security.core.userdetails.User(appUser.getUserName(), appUser.getPassword(), authorities);
        }

    }
}
