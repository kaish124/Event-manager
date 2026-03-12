package com.reza.events.security;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedUser {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private List<String> roles;
}
