package com.example.gotrip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                        // Users
                    auth.requestMatchers(HttpMethod.POST, "/agency/users/new").permitAll();

                        // Hotels
                    auth.requestMatchers(HttpMethod.GET, "/agency/hotels").permitAll();

                        // Flights
                    auth.requestMatchers(HttpMethod.GET, "/agency/flights").permitAll();

                        // Flights Booking
                    auth.requestMatchers(HttpMethod.POST, "/agency/flight-booking/new").permitAll();

                        // Hotels Booking
                    auth.requestMatchers(HttpMethod.POST, "/agency/room-booking/new").permitAll();

                        // Documentation Swagger
                    auth.requestMatchers("/doc/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll();

                    auth.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults()) // Autenticación por formulario
                .httpBasic(Customizer.withDefaults()) // Autenticación básica
                .build();
    }
}
