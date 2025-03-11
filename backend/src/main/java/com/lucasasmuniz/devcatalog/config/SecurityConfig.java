package com.lucasasmuniz.devcatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/**").permitAll() // Permite todas as rotas
            )
            .csrf(csrf -> csrf.disable()) // Desativa CSRF
            .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Permite o H2 Console

        return http.build();
    }
}
