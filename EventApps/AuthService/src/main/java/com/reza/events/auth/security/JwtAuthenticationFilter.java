package com.reza.events.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reza.events.auth.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;

public class JwtAuthenticationFilter extends AbstractJwtAuthFilter{

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, ObjectMapper objectMapper) {
        super(authenticationManager, jwtTokenUtil, objectMapper);
    }

    @Override
    protected boolean doAuthFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        String rawToken = request.getHeader("Authorization");
        if(rawToken == null || rawToken.isBlank())
            return true;
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
        response.setHeader("X-Token_Expires", String.valueOf(System.currentTimeMillis() / 1000 + JwtTokenUtil.getExpirySeconds()));
    }
}
