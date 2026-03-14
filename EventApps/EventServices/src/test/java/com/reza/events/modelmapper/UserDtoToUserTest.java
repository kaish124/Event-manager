package com.reza.events.modelmapper;

import com.reza.events.config.CommonConfig;
import com.reza.events.config.SecurityConfig;
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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {CommonConfig.class, UserToUserDto.class, UserRoleToRoleBean.class})
class UserDtoToUserTest {

    @Autowired
    private ModelMapper modelMapper;

    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        // Create test UserDto with roles
        RoleBean adminRoleBean = RoleBean.builder()
                .id(1L)
                .name("Event Admin")
                .description("Event administrator role")
                .type(UserRoleType.EVENT_ADMIN)
                .userRoleId(101L)
                .build();

        RoleBean userRoleBean = RoleBean.builder()
                .id(2L)
                .name("Attendee App User")
                .description("Attendee application user role")
                .type(UserRoleType.ATTENDEE_APP_USER)
                .userRoleId(102L)
                .build();

        List<RoleBean> roles = Arrays.asList(adminRoleBean, userRoleBean);

        testUserDto = UserDto.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .password("securePass123")
                .roles(roles)
                .build();
    }

    @Test
    void testUserDtoToUserMapping() {
        // When
        User user = modelMapper.map(testUserDto, User.class);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull(); // ID should be skipped during mapping
        assertThat(user.getFirstName()).isEqualTo("Jane");
        assertThat(user.getLastName()).isEqualTo("Smith");
        assertThat(user.getEmail()).isEqualTo("jane.smith@example.com");
//        assertThat(user.getPassword()).isEqualTo("securePass123");

        // Check roles mapping
        Set<UserRole> userRoles = user.getUserRoles();
        assertThat(userRoles).hasSize(2);

        // Check admin role mapping
        UserRole adminUserRole = userRoles.stream()
                .filter(ur -> ur.getRole().getType() == UserRoleType.EVENT_ADMIN)
                .findFirst()
                .orElse(null);
        assertThat(adminUserRole).isNotNull();
        assertThat(adminUserRole.getId()).isEqualTo(101L);
        
        Role adminRole = adminUserRole.getRole();
        assertThat(adminRole).isNotNull();
        assertThat(adminRole.getId()).isEqualTo(1L);
        assertThat(adminRole.getName()).isEqualTo("Event Admin");
        assertThat(adminRole.getDescription()).isEqualTo("Event administrator role");
        assertThat(adminRole.getType()).isEqualTo(UserRoleType.EVENT_ADMIN);

        // Check user role mapping
        UserRole regularUserRole = userRoles.stream()
                .filter(ur -> ur.getRole().getType() == UserRoleType.ATTENDEE_APP_USER)
                .findFirst()
                .orElse(null);
        assertThat(regularUserRole).isNotNull();
        assertThat(regularUserRole.getId()).isEqualTo(102L);
        
        Role userRole = regularUserRole.getRole();
        assertThat(userRole).isNotNull();
        assertThat(userRole.getId()).isEqualTo(2L);
        assertThat(userRole.getName()).isEqualTo("Attendee App User");
        assertThat(userRole.getDescription()).isEqualTo("Attendee application user role");
        assertThat(userRole.getType()).isEqualTo(UserRoleType.ATTENDEE_APP_USER);
    }

    @Test
    void testUserDtoToUserMappingWithNoRoles() {
        // Given
        testUserDto.setRoles(null);

        // When
        User user = modelMapper.map(testUserDto, User.class);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull(); // ID should be skipped during mapping
        assertThat(user.getFirstName()).isEqualTo("Jane");
        assertThat(user.getLastName()).isEqualTo("Smith");
        assertThat(user.getEmail()).isEqualTo("jane.smith@example.com");
//        assertThat(user.getPassword()).isEqualTo("securePass123");
        assertThat(user.getUserRoles()).isNull();
    }

    @Test
    void testUserDtoToUserMappingWithEmptyRoles() {
        // Given
        testUserDto.setRoles(Arrays.asList());

        // When
        User user = modelMapper.map(testUserDto, User.class);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull(); // ID should be skipped during mapping
        assertThat(user.getFirstName()).isEqualTo("Jane");
        assertThat(user.getLastName()).isEqualTo("Smith");
        assertThat(user.getEmail()).isEqualTo("jane.smith@example.com");
//        assertThat(user.getPassword()).isEqualTo("securePass123");
        assertThat(user.getUserRoles()).isEmpty();
    }
}