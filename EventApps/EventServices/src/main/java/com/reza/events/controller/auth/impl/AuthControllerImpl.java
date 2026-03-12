package com.reza.events.controller.auth.impl;

import com.reza.events.controller.auth.AuthController;
import com.reza.events.dto.LoginRequest;
import com.reza.events.dto.LoginResponse;
import com.reza.events.port.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services/auth")
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @RequestMapping("/login")
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        LoginResponse res = authService.login(loginRequest);
        return ResponseEntity.ok(res);
    }
}
