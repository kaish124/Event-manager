package com.reza.events.port.auth.impl;

import com.reza.events.auth.security.LoginAuthenticationToken;
import com.reza.events.auth.util.JwtTokenUtil;
import com.reza.events.security.AuthenticatedUser;
import com.reza.events.dto.LoginRequest;
import com.reza.events.dto.LoginResponse;
import com.reza.events.port.auth.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper mapper;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, ModelMapper mapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.mapper = mapper;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        var authToken = mapper.map(loginRequest, LoginAuthenticationToken.class);

        Authentication auth = authenticationManager.authenticate(authToken);
        AuthenticatedUser user = (AuthenticatedUser) auth.getPrincipal();

        String token = jwtTokenUtil.generateToken(user);
        return LoginResponse.of(token, JwtTokenUtil.getExpirySeconds());
    }
}
