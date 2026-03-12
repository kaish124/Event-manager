package com.reza.events.port;

import com.reza.events.security.AuthenticatedUser;
import jakarta.validation.constraints.NotBlank;

public interface UserQueryPort {
    AuthenticatedUser loadUser(@NotBlank String email);
    AuthenticatedUser loadUser(@NotBlank Long UserId);
}
