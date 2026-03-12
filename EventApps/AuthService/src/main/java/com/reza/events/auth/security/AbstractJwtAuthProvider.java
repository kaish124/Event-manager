package com.reza.events.auth.security;

import com.reza.events.security.AuthenticatedUser;
import com.reza.events.port.UserQueryPort;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractJwtAuthProvider implements AuthenticationProvider {

    protected final UserQueryPort userQueryPort;
    protected final RoleHierarchyService roleHierarchyService;

    protected AbstractJwtAuthProvider(UserQueryPort userQueryPort, RoleHierarchyService roleHierarchyService) {
        this.userQueryPort = userQueryPort;
        this.roleHierarchyService = roleHierarchyService;
    }

    protected AuthenticatedUser getCurrentUser(Long userId){

        AuthenticatedUser user = userQueryPort.loadUser(userId);
        if(user == null){
            throw new UsernameNotFoundException("User not found: " + userId);
        }
        return user;
    }

    protected Collection<GrantedAuthority> expandAuthorities(AuthenticatedUser user){
        var baseAuthority = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());

        return roleHierarchyService.getReachableAuthorities(baseAuthority);
    }
}
