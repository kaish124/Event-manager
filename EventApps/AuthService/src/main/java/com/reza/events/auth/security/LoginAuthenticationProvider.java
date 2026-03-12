package com.reza.events.auth.security;

import com.reza.events.port.UserQueryPort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginAuthenticationProvider extends AbstractJwtAuthProvider{
    private final PasswordEncoder passwordEncoder;

    public  LoginAuthenticationProvider(UserQueryPort userQueryPort, RoleHierarchyService roleHierarchyService, PasswordEncoder passwordEncoder) {
        super(userQueryPort, roleHierarchyService);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginAuthenticationToken token = (LoginAuthenticationToken) authentication;

        var user = userQueryPort.loadUser(token.getEmail());

        if(user == null){
            throw new BadCredentialsException("Invalid email or password");
        }

        if(!passwordEncoder.matches(token.getCredentials().toString(), user.getPassword())){
            throw new BadCredentialsException("Invalid credentials");
        }

        var authorities = expandAuthorities(user);

        return new JwtAuthenticationToken(user, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
