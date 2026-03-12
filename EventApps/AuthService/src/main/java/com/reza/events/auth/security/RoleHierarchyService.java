package com.reza.events.auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

public class RoleHierarchyService {
    private static final Map<String, Set<String>> HIERARCHY = new HashMap<>();

    static {
        HIERARCHY.put("GLOBAL_ADMIN", Set.of("EVENT_ADMIN", "ETHICS_ADMIN"));
        HIERARCHY.put("EVENT_ADMIN", Set.of("EVENT_MANAGER", "CURATOR", "EVENT_ADMIN_LITE", "REQUESTER"));
        HIERARCHY.put("EVENT_MANAGER", Set.of("CURATOR", "REQUESTER"));
        HIERARCHY.put("CURATOR", Set.of("REQUESTER"));
        HIERARCHY.put("ETHICS_ADMIN", Set.of());
        HIERARCHY.put("REQUESTER", Set.of());
    }

    public Collection<GrantedAuthority> getReachableAuthorities(Collection<? extends GrantedAuthority> baseAuthorities) {
        Set<GrantedAuthority> reachable = new HashSet<>(baseAuthorities);

        for(GrantedAuthority grantedAuthority : baseAuthorities){
            String roleName = stripRolePrefix(grantedAuthority.getAuthority());
            expand(roleName, reachable);
        }

        return reachable;
    }

    private void expand(String roleName, Set<GrantedAuthority> authorities) {
        Set<String> implied = HIERARCHY.getOrDefault(roleName, Set.of());
        for(String impliedRole : implied){
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + impliedRole);
            if(authorities.add(authority)){
                expand(impliedRole, authorities);
            }
        }
    }

    private String stripRolePrefix(String authority){
        return authority.startsWith("ROLE_") ? authority.substring(5) : authority;
    }
}
