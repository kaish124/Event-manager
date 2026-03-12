package com.reza.events.controller.auth;

import com.reza.events.dto.LoginRequest;
import com.reza.events.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface AuthController {

    public ResponseEntity<LoginResponse> login(@Valid LoginRequest loginRequest);
}
