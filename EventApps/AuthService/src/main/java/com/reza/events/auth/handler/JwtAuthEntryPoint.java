package com.reza.events.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reza.events.auth.exception.AuthException;
import com.reza.events.exception.ErrorResponse;
import com.reza.events.logging.LogKeys;
import com.reza.events.logging.Logger;
import com.reza.events.logging.LoggerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthEntryPoint.class);
    private final ObjectMapper objectMapper;

    public JwtAuthEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOGGER.warn(LogKeys.MSG, "Authentication failed", LogKeys.URI, request.getRequestURI(), LogKeys.ERROR_MESSAGE, authException.getMessage());

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                resolveErrorCode(authException),
                authException.getMessage(),
                request.getRequestURI()
        );

        writeResponse(response, HttpStatus.UNAUTHORIZED.value(), body);
    }
    private String resolveErrorCode(AuthenticationException ex) {
        if(ex instanceof AuthException authEx) return authEx.getErrorCode();
        return "auth.failed";
    }

    private void writeResponse(HttpServletResponse response, int status, ErrorResponse body) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
