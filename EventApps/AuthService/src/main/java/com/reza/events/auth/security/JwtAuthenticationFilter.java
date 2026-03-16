package com.reza.events.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reza.events.auth.handler.JwtAuthEntryPoint;
import com.reza.events.auth.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;

public class JwtAuthenticationFilter extends AbstractJwtAuthFilter{

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, ObjectMapper objectMapper, JwtAuthEntryPoint authEntryPoint) {
        super(authenticationManager, jwtTokenUtil, objectMapper, authEntryPoint);
    }

    @Override
    protected boolean doAuthFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || authHeader.isBlank())
            return true;
            
        // Handle both "Bearer token" and raw token formats
        String rawToken;
        if(authHeader.startsWith("Bearer ")) {
            rawToken = authHeader.substring(7);
        } else {
            rawToken = authHeader;
        }
        
        JwtAuthenticationToken preAuthToken = new JwtAuthenticationToken(rawToken);
        processTokenAuthentication(preAuthToken);
        issueRefreshedToken(response);
        return true;
    }

    private void issueRefreshedToken(HttpServletResponse response) {
        var currentUser = SecurityUtil.getCurrentUser();
        if(currentUser == null) return;
        String newToken = jwtTokenUtil.generateToken(currentUser);
        response.setHeader("X-Refreshed-Token", "Bearer " + newToken);
        response.setHeader("X-Token-Expires", String.valueOf(System.currentTimeMillis() / 1000 + jwtTokenUtil.getExpirySeconds()));
    }
}
