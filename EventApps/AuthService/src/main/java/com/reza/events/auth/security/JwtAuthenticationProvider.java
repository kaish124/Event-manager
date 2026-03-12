package com.reza.events.auth.security;

import com.reza.events.auth.util.JwtTokenUtil;
import com.reza.events.port.UserQueryPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationProvider extends AbstractJwtAuthProvider{

    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationProvider(UserQueryPort userQueryPort, RoleHierarchyService roleHierarchyService, JwtTokenUtil jwtTokenUtil) {
        super(userQueryPort, roleHierarchyService);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Claims claims = validateToken(token.getRawToken());
        Long userId = claims.get("userId", Long.class);

        var user = getCurrentUser(userId);
        var authorities = expandAuthorities(user);

         return new JwtAuthenticationToken(user, authorities);
    }

    private Claims validateToken(String rawToken) {
        try{
            return jwtTokenUtil.parseToken(rawToken);
        }catch (ExpiredJwtException ex){
            throw new InsufficientAuthenticationException("Token has expired", ex);
        }catch (Exception ex){
            throw new InsufficientAuthenticationException("Invalid token", ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
