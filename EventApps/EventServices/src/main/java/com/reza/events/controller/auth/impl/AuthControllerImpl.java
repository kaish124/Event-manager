package com.reza.events.controller.auth.impl;

import com.reza.events.controller.auth.AuthController;
import com.reza.events.dto.LoginRequest;
import com.reza.events.dto.LoginResponse;
import com.reza.events.http.RequestStateHolder;
import com.reza.events.logging.LogKeys;
import com.reza.events.logging.Logger;
import com.reza.events.logging.LoggerFactory;
import com.reza.events.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services/auth")
public class AuthControllerImpl implements AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthControllerImpl.class);
    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @RequestMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LOGGER.debug(LogKeys.MSG, "Login attempt", LogKeys.EMAIL, loginRequest.getEmail());
        LoginResponse res = authService.login(loginRequest);
        LOGGER.info(LogKeys.MSG, "Login successful", LogKeys.EMAIL, loginRequest.getEmail(), LogKeys.USER_ID, RequestStateHolder.getUserId());
        return ResponseEntity.ok(res);
    }
}