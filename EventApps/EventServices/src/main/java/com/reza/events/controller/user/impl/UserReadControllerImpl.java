package com.reza.events.controller.user.impl;

import com.reza.events.controller.user.UserReadController;
import com.reza.events.dto.UserDto;
import com.reza.events.logging.LogKeys;
import com.reza.events.logging.Logger;
import com.reza.events.logging.LoggerFactory;
import com.reza.events.service.user.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserReadControllerImpl implements UserReadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserReadControllerImpl.class);
    private final UserReadService userReadService;

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) {
        LOGGER.debug(LogKeys.MSG, "Get user by ID", LogKeys.USER_ID, id);
        UserDto user = userReadService.getUserById(id);
        LOGGER.info(LogKeys.MSG, "Returning user", LogKeys.USER_ID, id);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        LOGGER.debug(LogKeys.MSG, "Get all users");
        List<UserDto> users = userReadService.getAllUsers();
        LOGGER.info(LogKeys.MSG, "Returning users", "COUNT", users.size());
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserDto> getUserByEmail(String email) {
        LOGGER.debug(LogKeys.MSG, "Get user by email", LogKeys.EMAIL, email);
        UserDto user = userReadService.getUserByEmail(email);
        LOGGER.info(LogKeys.MSG, "Returning user", LogKeys.EMAIL, email);
        return ResponseEntity.ok(user);
    }
}
