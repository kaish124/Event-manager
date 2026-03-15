package com.reza.events.enums;

import com.reza.events.security.UserPermission;

import java.util.Arrays;
import java.util.Set;

public enum UserRoleType {

    REQUESTER(
            UserPermission.CREATE_EVENT,
            UserPermission.VIEW_EVENT,
            UserPermission.MANAGE_REGISTRATION
    ),

    CURATOR(
            UserPermission.VIEW_EVENT,
            UserPermission.VIEW_REGISTRATION,
            UserPermission.MANAGE_REGISTRATION
    ),

    EVENT_MANAGER(
            UserPermission.VIEW_EVENT,
            UserPermission.VIEW_REGISTRATION,
            UserPermission.VIEW_USER
    ),

    EVENT_ADMIN(
            UserPermission.CREATE_EVENT,
            UserPermission.UPDATE_EVENT,
            UserPermission.DELETE_EVENT,
            UserPermission.VIEW_EVENT,
            UserPermission.VIEW_REGISTRATION,
            UserPermission.MANAGE_REGISTRATION,
            UserPermission.VIEW_USER
    ),
    SUPER_ADMIN(
            UserPermission.MANAGE_USER,
            UserPermission.VIEW_USER
    );


    private final Set<UserPermission> userPermissions;

    UserRoleType(UserPermission... userPermissions) {
        this.userPermissions = Set.copyOf(Arrays.asList(userPermissions));
    }

    public Set<UserPermission> getUserPermissions() {
        return userPermissions;
    }
}
