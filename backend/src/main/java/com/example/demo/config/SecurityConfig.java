package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // disable for now (API use)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/landing",
                    "/auth/login",
                    "/auth/signup",
                    "/hello",
                    "/message"
                ).permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}