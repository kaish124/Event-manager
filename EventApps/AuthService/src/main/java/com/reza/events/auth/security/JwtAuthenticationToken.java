package com.reza.events.auth.security;


import com.reza.events.security.AuthenticatedUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken{
    private final String rawToken;
    private final AuthenticatedUser principal;

    public JwtAuthenticationToken(String token) {
        super(null);
        this.rawToken = token;
        this.principal = null;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(AuthenticatedUser principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.rawToken = null;
        this.principal = principal;
        setAuthenticated(true);
    }
    @Override
    public Object getCredentials() {
        return rawToken;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getRawToken() {
        return rawToken;
    }
}
