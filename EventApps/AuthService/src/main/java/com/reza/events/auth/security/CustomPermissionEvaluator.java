package com.reza.events.auth.security;

import com.reza.events.security.UserPermission;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if(authentication == null || !(permission instanceof String permName)) {
            return false;
        }
        return hasPermissionAuthority(authentication, permName);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if(authentication == null || !(permission instanceof String permName)) {
            return false;
        }
        return hasPermissionAuthority(authentication, permName);
    }

    private boolean hasPermissionAuthority(Authentication authentication, String permName) {
        try{
            UserPermission.valueOf(permName);
        }catch(IllegalArgumentException e) {
            return false;
        }

        String requiredAuthority = "PERM" +  permName;
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(requiredAuthority::equals);
    }
}
