package com.reza.events.service.auth;

import com.reza.events.dto.LoginRequest;
import com.reza.events.dto.LoginResponse;

/**
 * Authentication service interface.
 * Provides methods for user authentication and token management.
 */
public interface AuthService {

    /**
     * Authenticates a user with their credentials and returns a JWT token.
     *
     * @param loginRequest the login request containing email and password
     * @return LoginResponse containing JWT token and user information
     */
    public LoginResponse login(LoginRequest loginRequest);
}
