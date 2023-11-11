package com.hendisantika.recaptchav3;

import com.hendisantika.recaptchav3.entity.Role;
import com.hendisantika.recaptchav3.entity.User;
import com.hendisantika.recaptchav3.repository.RoleRepository;
import com.hendisantika.recaptchav3.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class SpringBootGoogleRecaptchaSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGoogleRecaptchaSampleApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository appUserRepository,
                                        RoleRepository roleRepository,
                                        PasswordEncoder passwordEncoder) {
        return args -> {
            List<Role> roles = List.of(
                    new Role("ROLE_USER"),
                    new Role("ROLE_ADMIN")
            );

            roleRepository.saveAll(roles);

            List<User> appUsers = List.of(
                    new User("naruto", passwordEncoder.encode("1234")),
                    new User("gojo", passwordEncoder.encode("1234"))
            );
            appUserRepository.saveAll(appUsers);

            User naruto = appUserRepository.findAppUserByUserName("naruto");
            Role roleForJohn = roleRepository.findRoleByRoleName("ROLE_USER");
            naruto.getRoles().add(roleForJohn);
            appUserRepository.save(naruto);


            User gojo = appUserRepository.findAppUserByUserName("gojo");
            Role roleForGojo = roleRepository.findRoleByRoleName("ROLE_ADMIN");
            gojo.getRoles().add(roleForGojo);
            appUserRepository.save(gojo);
        };
    }

}
