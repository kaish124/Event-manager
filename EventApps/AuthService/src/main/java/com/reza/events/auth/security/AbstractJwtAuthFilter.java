package com.reza.events.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reza.events.auth.handler.JwtAuthEntryPoint;
import com.reza.events.auth.util.JwtTokenUtil;
import com.reza.events.http.RequestStateHolder;
import com.reza.events.logging.LogKeys;
import com.reza.events.logging.Logger;
import com.reza.events.logging.LoggerFactory;
import com.reza.events.security.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public abstract class AbstractJwtAuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJwtAuthFilter.class);

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

        LOGGER.debug(LogKeys.MSG, "JWT Filter processing request", LogKeys.METHOD, method, LogKeys.URI, requestURI);

        try {
            boolean proceed = doAuthFilter(request, response, chain);
            if (proceed) {
                chain.doFilter(request, response);
            }
        } catch (AuthenticationException e) {
            authEntryPoint.commence(request, response, e);
        } catch (Exception e) {
            LOGGER.error(e, LogKeys.MSG, "Unexpected error during filter processing", LogKeys.METHOD, method, LogKeys.URI, requestURI);
            throw new InternalAuthenticationServiceException("Internal error during authentication", e);
        } finally {
        }
    }

    protected abstract boolean doAuthFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain);

    protected void processTokenAuthentication(Authentication preAuthToken) {
        LOGGER.debug(LogKeys.MSG, "Processing token authentication");
        Authentication auth = authenticationManager.authenticate(preAuthToken);
        SecurityUtil.setCurrentUser(auth);

        if (auth.getPrincipal() instanceof AuthenticatedUser user) {
            RequestStateHolder.setUserId(user.getId());
            LOGGER.debug(LogKeys.MSG, "User authenticated", LogKeys.USER_ID, user.getId());
        }
    }

    protected String extractBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            LOGGER.debug(LogKeys.MSG, "Bearer token found in request");
            return bearerToken.substring(7);
        }
        LOGGER.debug(LogKeys.MSG, "No bearer token found in request");
        return null;
    }
}
