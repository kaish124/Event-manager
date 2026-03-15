package com.reza.events.auth.security;

import com.reza.events.enums.UserRoleType;
import com.reza.events.security.UserPermission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class RoleHierarchyService {
    private static final Map<UserRoleType, Set<UserRoleType>> HIERARCHY = new HashMap<>();

    static {
        HIERARCHY.put(UserRoleType.SUPER_ADMIN, Set.of(UserRoleType.EVENT_ADMIN));
        HIERARCHY.put(UserRoleType.EVENT_ADMIN, Set.of(UserRoleType.EVENT_MANAGER, UserRoleType.CURATOR));
        HIERARCHY.put(UserRoleType.EVENT_MANAGER, Set.of(UserRoleType.CURATOR, UserRoleType.REQUESTER));
        HIERARCHY.put(UserRoleType.CURATOR, Set.of(UserRoleType.REQUESTER));
        HIERARCHY.put(UserRoleType.REQUESTER, Set.of());
    }

    public Collection<GrantedAuthority> buildAuthorities(UserRoleType baseRole) {
        Set<UserRoleType> reachableRoles = expandRoles(baseRole);

        Set<GrantedAuthority> authorities = new HashSet<>();

        for(UserRoleType reachableRole : reachableRoles){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + reachableRole.name()));
        }

        Set<UserPermission> effectivePermissions = collectPermission(reachableRoles);
        for(UserPermission permission : effectivePermissions){
            authorities.add(new SimpleGrantedAuthority("PERM_" + permission.name()));
        }
        return authorities;
    }

    private Set<UserRoleType> expandRoles(UserRoleType baseRole) {
        Set<UserRoleType> reachable = EnumSet.noneOf(UserRoleType.class);
        reachable.add(baseRole);
        expandRecursive(baseRole, reachable);
        return reachable;
    }

    private void expandRecursive(UserRoleType baseRole, Set<UserRoleType> reachable) {
        Set<UserRoleType> currentRoles = HIERARCHY.get(baseRole);
        for (UserRoleType role : currentRoles) {
            if (reachable.add(role)) {
               expandRecursive(role, reachable);
            }
        }
    }

    private Set<UserPermission> collectPermission(Set<UserRoleType> reachableRoles) {
        Set<UserPermission> effectivePermissions = EnumSet.noneOf(UserPermission.class);
        for(UserRoleType reachableRole : reachableRoles){
            effectivePermissions.addAll(reachableRole.getUserPermissions());
        }
        return effectivePermissions;
    }

}
