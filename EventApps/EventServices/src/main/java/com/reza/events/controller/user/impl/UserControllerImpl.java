package com.reza.events.controller.user.impl;

import com.reza.events.dto.UserDto;
import com.reza.events.service.UserReadService;
import com.reza.events.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/services/user")
@RequiredArgsConstructor
public class UserControllerImpl {

    private final UserReadService userReadService;
    private final UserWriteService userWriteService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.debug("GET /services/user/{}", id);
        UserDto user = userReadService.getUserById(id);
        log.info("Returning user with id: {}", id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.debug("GET /services/user");
        List<UserDto> users = userReadService.getAllUsers();
        log.info("Returning {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        log.debug("GET /services/user/email/{}", email);
        UserDto user = userReadService.getUserByEmail(email);
        log.info("Returning user with email: {}", email);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.debug("POST /services/user - Creating user with email: {}", userDto.getEmail());
        UserDto createdUser = userWriteService.createUser(userDto);
        log.info("User created successfully with id: {}", createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        log.debug("PUT /services/user/{} - Updating user", id);
        UserDto updatedUser = userWriteService.updateUser(id, userDto);
        log.info("User updated successfully with id: {}", id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("DELETE /services/user/{} - Deleting user", id);
        userWriteService.deleteUser(id);
        log.info("User deleted successfully with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
