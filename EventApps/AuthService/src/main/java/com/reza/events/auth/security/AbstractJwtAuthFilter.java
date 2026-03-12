package com.reza.events.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reza.events.auth.util.JwtTokenUtil;
import com.reza.events.security.AuthenticatedUser;
import com.reza.events.http.RequestStateHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractJwtAuthFilter extends OncePerRequestFilter {

    protected final AuthenticationManager authenticationManager;
    protected final JwtTokenUtil jwtTokenUtil;
    protected final ObjectMapper objectMapper;

    public AbstractJwtAuthFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        RequestStateHolder.setupRequest(request);
        try{
            boolean proceed = doAuthFilter(request, response, chain);
            if(proceed) {
                chain.doFilter(request, response);
            }
        }catch(AuthenticationException e){
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }catch(Exception e){
            sendError(response, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }finally{
            RequestStateHolder.teardownRequest();
        }
    }

    protected abstract boolean doAuthFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain);

    protected void processTokenAuthentication(Authentication preAuthToken) {
        Authentication auth = authenticationManager.authenticate(preAuthToken);
        SecurityUtil.setCurrentUser(auth);

        if(auth.getPrincipal() instanceof AuthenticatedUser user){
            RequestStateHolder.setUserId(user.getId());
        }
    }

    protected String extractBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(Map.of("error", message, "status", status)));
    }
}
