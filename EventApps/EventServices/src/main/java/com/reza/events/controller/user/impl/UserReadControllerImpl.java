package com.reza.events.controller.user.impl;

import com.reza.events.controller.user.UserReadController;
import com.reza.events.dto.UserDto;
import com.reza.events.service.user.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserReadControllerImpl implements UserReadController {

    private final UserReadService userReadService;

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) {
        log.debug("GET /services/user/{}", id);
        UserDto user = userReadService.getUserById(id);
        log.info("Returning user with id: {}", id);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.debug("GET /services/user");
        List<UserDto> users = userReadService.getAllUsers();
        log.info("Returning {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserDto> getUserByEmail(String email) {
        log.debug("GET /services/user/email/{}", email);
        UserDto user = userReadService.getUserByEmail(email);
        log.info("Returning user with email: {}", email);
        return ResponseEntity.ok(user);
    }
}