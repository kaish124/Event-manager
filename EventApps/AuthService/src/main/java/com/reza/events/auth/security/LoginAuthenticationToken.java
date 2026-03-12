package com.reza.events.auth.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class LoginAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;
    private final String password;

    public LoginAuthenticationToken(String email1, String password1) {
        super(null);
        this.email = email1;
        this.password = password1;
        setAuthenticated(false);
    }
    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    public String getEmail() {
        return email;
    }
}
