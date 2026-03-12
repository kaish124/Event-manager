package com.reza.events.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reza.events.auth.security.JwtAuthenticationFilter;
import com.reza.events.auth.security.JwtAuthenticationProvider;
import com.reza.events.auth.security.LoginAuthenticationProvider;
import com.reza.events.auth.security.RoleHierarchyService;
import com.reza.events.auth.util.JwtTokenUtil;
import com.reza.events.port.UserQueryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(0)
    public SecurityFilterChain actuatorChain(HttpSecurity http) throws Exception {
        return http.securityMatcher("/actuator/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").access(new WebExpressionAuthorizationManager("hasIpAddress('127.0.0.1') or hasIpAddress('::1')")))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain publicChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/auth/**", "/services/public/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain servicesChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        return http
                .securityMatcher("/services/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil();
    }

    @Bean
    public RoleHierarchyService roleHierarchyService(){
        return new RoleHierarchyService();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            JwtAuthenticationProvider jwtProvider,
            LoginAuthenticationProvider loginProvider){
        return new ProviderManager(List.of(jwtProvider, loginProvider));
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(UserQueryPort userQueryPort, RoleHierarchyService roleHierarchyService, JwtTokenUtil jwtTokenUtil){
        return new JwtAuthenticationProvider(userQueryPort, roleHierarchyService, jwtTokenUtil);
    }

    @Bean
    public LoginAuthenticationProvider loginAuthenticationProvider(UserQueryPort userQueryPort, RoleHierarchyService roleHierarchyService, PasswordEncoder passwordEncoder){
        return new LoginAuthenticationProvider(userQueryPort, roleHierarchyService, passwordEncoder);
    }

    @Bean
    public PasswordEncoder  passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtTokenUtil JwtTokenUtil,
            ObjectMapper objectMapper){
        return new  JwtAuthenticationFilter(authenticationManager, JwtTokenUtil, objectMapper);
    }



}
