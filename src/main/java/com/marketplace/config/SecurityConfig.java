package com.marketplace.config;

import com.marketplace.repository.user.JdbcUserRepository;
import com.marketplace.repository.user.UserRepository;
import com.marketplace.service.user.UserService;
import com.marketplace.service.user.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig{

    @Bean
    public UserService userService(UserRepository repository, PasswordEncoder encoder) {
        return new UserServiceImpl(repository, encoder);
    }

    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcUserRepository(jdbcTemplate);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService service) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(service).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll()

                .and()
                .formLogin()
                .loginPage("/users/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/categories", true)
                .failureUrl("/users/login")

                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/categories")

                .and()
                .authenticationManager(authenticationManager);

        return http.build();
    }
}
