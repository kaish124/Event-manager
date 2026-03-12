package com.reza.events.port.auth;

import com.reza.events.dto.LoginRequest;
import com.reza.events.dto.LoginResponse;

public interface AuthService {

    /**
     * User login
     * @param loginRequest email and password
     * @return LoginResponse with token details
     */
    public LoginResponse login(LoginRequest loginRequest);
}
