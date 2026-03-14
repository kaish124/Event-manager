package com.reza.events.dto;

import com.reza.events.enums.UserRoleType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleBean {

    private Long id;

    private String name;

    private Long userRoleId;

    private String description;

    private UserRoleType type;
}
