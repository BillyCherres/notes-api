package com.williamcherres.notes_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // IMPORTANT: uses CorsConfigurationSource bean
                .authorizeHttpRequests(auth -> auth
                        // allow preflight through without auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        
                        .requestMatchers("/health").permitAll()

                        .requestMatchers(HttpMethod.HEAD, "/health").permitAll()

                        // allow health if you want
                        .requestMatchers("/actuator/health").permitAll()

                        // everything else requires JWT
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}