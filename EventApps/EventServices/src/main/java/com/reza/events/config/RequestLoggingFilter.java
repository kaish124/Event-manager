package com.reza.events.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            
            String logMessage = String.format("%s %s - Status: %d - Duration: %dms", 
                method, requestURI, status, duration);
            
            if (status >= 400) {
                log.warn(logMessage);
            } else {
                log.info(logMessage);
            }
        }
    }
}