package com.reza.events.auth.security;

import com.reza.events.enums.UserRoleType;
import com.reza.events.security.AuthenticatedUser;
import com.reza.events.port.UserQueryPort;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if(user.getRoles() == null || user.getRoles().isEmpty()){
            return List.of();
        }

        Set<GrantedAuthority> authorities = new HashSet<>();

        for(String roleName : user.getRoles()){
            UserRoleType roleType = UserRoleType.valueOf(roleName);
            authorities.addAll(roleHierarchyService.buildAuthorities(roleType));
        }

        return authorities;
    }
}
