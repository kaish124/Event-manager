package com.reza.events.controller.user.impl;

import com.reza.events.controller.user.UserWriteController;
import com.reza.events.dto.UserDto;
import com.reza.events.service.user.UserWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/services/user")
@RequiredArgsConstructor
public class UserWriteControllerImpl implements UserWriteController {

    private final UserWriteService userWriteService;

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDto> createUser(UserDto userDto) {
        log.debug("POST /services/user - Creating user with email: {}", userDto.getEmail());
        UserDto createdUser = userWriteService.createUser(userDto);
        log.info("User created successfully with id: {}", createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDto> updateUser(Long id, UserDto userDto) {
        log.debug("PUT /services/user/{} - Updating user", id);
        UserDto updatedUser = userWriteService.updateUser(id, userDto);
        log.info("User updated successfully with id: {}", id);
        return ResponseEntity.ok(updatedUser);
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteUser(Long id) {
        log.debug("DELETE /services/user/{} - Deleting user", id);
        userWriteService.deleteUser(id);
        log.info("User deleted successfully with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}