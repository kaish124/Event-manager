package com.reza.events.modelmapper;

import com.reza.events.config.CommonConfig;
import com.reza.events.domain.entity.Role;
import com.reza.events.domain.entity.User;
import com.reza.events.domain.entity.UserRole;
import com.reza.events.dto.RoleBean;
import com.reza.events.dto.UserDto;
import com.reza.events.enums.UserRoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {CommonConfig.class, UserToUserDto.class, UserRoleToRoleBean.class})
class UserToUserDtoTest {

    @Autowired
    private ModelMapper modelMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Create test user with roles
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("password123");

        // Create roles
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("Event Admin");
        adminRole.setDescription("Event administrator role");
        adminRole.setType(UserRoleType.EVENT_ADMIN);

        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("Attendee App User");
        userRole.setDescription("Attendee application user role");
        userRole.setType(UserRoleType.REQUESTER);

        // Create user roles
        UserRole adminUserRole = new UserRole();
        adminUserRole.setId(101L);
        adminUserRole.setRole(adminRole);
        adminUserRole.setUser(testUser);

        UserRole regularUserRole = new UserRole();
        regularUserRole.setId(102L);
        regularUserRole.setRole(userRole);
        regularUserRole.setUser(testUser);

        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(adminUserRole);
        userRoles.add(regularUserRole);
        testUser.setUserRoles(userRoles);
    }

    @Test
    void testUserToUserDtoMapping() {
        // When
        UserDto userDto = modelMapper.map(testUser, UserDto.class);

        // Then
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getFirstName()).isEqualTo("John");
        assertThat(userDto.getLastName()).isEqualTo("Doe");
        assertThat(userDto.getEmail()).isEqualTo("john.doe@example.com");
//        assertThat(userDto.getPassword()).isEqualTo("password123");

        // Check roles mapping
        List<RoleBean> roles = userDto.getRoles();
        assertThat(roles).hasSize(2);

        // Check admin role
        RoleBean adminRoleBean = roles.stream()
                .filter(r -> r.getType() == UserRoleType.EVENT_ADMIN)
                .findFirst()
                .orElse(null);
        assertThat(adminRoleBean).isNotNull();
        assertThat(adminRoleBean.getId()).isEqualTo(1L);
        assertThat(adminRoleBean.getName()).isEqualTo("Event Admin");
        assertThat(adminRoleBean.getDescription()).isEqualTo("Event administrator role");
        assertThat(adminRoleBean.getUserRoleId()).isEqualTo(101L);

        // Check user role
        RoleBean userRoleBean = roles.stream()
                .filter(r -> r.getType() == UserRoleType.REQUESTER)
                .findFirst()
                .orElse(null);
        assertThat(userRoleBean).isNotNull();
        assertThat(userRoleBean.getId()).isEqualTo(2L);
        assertThat(userRoleBean.getName()).isEqualTo("Attendee App User");
        assertThat(userRoleBean.getDescription()).isEqualTo("Attendee application user role");
        assertThat(userRoleBean.getUserRoleId()).isEqualTo(102L);
    }

    @Test
    void testUserToUserDtoMappingWithNoRoles() {
        // Given
        testUser.setUserRoles(null);

        // When
        UserDto userDto = modelMapper.map(testUser, UserDto.class);

        // Then
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getFirstName()).isEqualTo("John");
        assertThat(userDto.getLastName()).isEqualTo("Doe");
        assertThat(userDto.getEmail()).isEqualTo("john.doe@example.com");
//        assertThat(userDto.getPassword()).isEqualTo("password123");
        assertThat(userDto.getRoles()).isNull();
    }
}