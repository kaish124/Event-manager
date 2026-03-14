package com.reza.events.controller.auth.impl;

import com.reza.events.controller.auth.AuthController;
import com.reza.events.dto.LoginRequest;
import com.reza.events.dto.LoginResponse;
import com.reza.events.port.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/services/auth")
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @RequestMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.debug("Login attempt for user: {}", loginRequest.getEmail());
        LoginResponse res = authService.login(loginRequest);
        log.info("Login successful for user: {}", loginRequest.getEmail());
        return ResponseEntity.ok(res);
    }
}
