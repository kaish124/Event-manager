package com.reza.events.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reza.events.auth.exception.TokenExpiredException;
import com.reza.events.auth.exception.TokenInvalidException;
import com.reza.events.auth.handler.JwtAuthEntryPoint;
import com.reza.events.auth.util.JwtTokenUtil;
import com.reza.events.security.AuthenticatedUser;
import com.reza.events.http.RequestStateHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public abstract class AbstractJwtAuthFilter extends OncePerRequestFilter {

    protected final AuthenticationManager authenticationManager;
    protected final JwtTokenUtil jwtTokenUtil;
    protected final ObjectMapper objectMapper;
    protected final JwtAuthEntryPoint authEntryPoint;

    public AbstractJwtAuthFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, ObjectMapper objectMapper, JwtAuthEntryPoint authEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.objectMapper = objectMapper;
        this.authEntryPoint = authEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        log.debug("JWT Filter processing {} {}", method, requestURI);
        RequestStateHolder.setupRequest(request);

        try {
            boolean proceed = doAuthFilter(request, response, chain);
            if (proceed) {
                chain.doFilter(request, response);
            }
        } catch (AuthenticationException e) {
            authEntryPoint.commence(request, response, e);
        } catch (Exception e) {
            log.error("Unexpected error during filter processing {} {}: {}", method, requestURI, e.getMessage(), e);
            throw new InternalAuthenticationServiceException("Internal error during authentication", e);
        } finally {
            RequestStateHolder.teardownRequest();
        }
    }

    protected abstract boolean doAuthFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain);

    protected void processTokenAuthentication(Authentication preAuthToken) {
        log.debug("Processing token authentication");
        Authentication auth = authenticationManager.authenticate(preAuthToken);
        SecurityUtil.setCurrentUser(auth);

        if (auth.getPrincipal() instanceof AuthenticatedUser user) {
            RequestStateHolder.setUserId(user.getId());
            log.debug("User authenticated with id: {}", user.getId());
        }
    }

    protected String extractBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            log.debug("Bearer token found in request");
            return bearerToken.substring(7);
        }
        log.debug("No bearer token found in request");
        return null;
    }
}
