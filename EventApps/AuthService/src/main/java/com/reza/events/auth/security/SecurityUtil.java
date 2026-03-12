package com.reza.events.auth.security;

import com.reza.events.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {}

    public static AuthenticatedUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth instanceof JwtAuthenticationToken token && token.isAuthenticated()) {
            return (AuthenticatedUser) token.getPrincipal();
        }
        return null;
    }

    public static void setCurrentUser(Authentication auth) {
        var ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(auth);
        SecurityContextHolder.setContext(ctx);
    }

}
