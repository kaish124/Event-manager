package com.reza.events.controller.user.impl;

import com.reza.events.controller.user.UserWriteController;
import com.reza.events.dto.UserDto;
import com.reza.events.logging.LogKeys;
import com.reza.events.logging.Logger;
import com.reza.events.logging.LoggerFactory;
import com.reza.events.service.user.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserWriteControllerImpl implements UserWriteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserWriteControllerImpl.class);
    private final UserWriteService userWriteService;

    @Override
    public ResponseEntity<UserDto> createUser(UserDto userDto) {
        LOGGER.debug(LogKeys.MSG, "Creating user", LogKeys.EMAIL, userDto.getEmail());
        UserDto createdUser = userWriteService.createUser(userDto);
        LOGGER.info(LogKeys.MSG, "User created successfully", LogKeys.USER_ID, createdUser.getId(), LogKeys.EMAIL, createdUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Override
    public ResponseEntity<UserDto> updateUser(Long id, UserDto userDto) {
        LOGGER.debug(LogKeys.MSG, "Updating user", LogKeys.USER_ID, id);
        UserDto updatedUser = userWriteService.updateUser(id, userDto);
        LOGGER.info(LogKeys.MSG, "User updated successfully", LogKeys.USER_ID, id);
        return ResponseEntity.ok(updatedUser);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        LOGGER.debug(LogKeys.MSG, "Deleting user", LogKeys.USER_ID, id);
        userWriteService.deleteUser(id);
        LOGGER.info(LogKeys.MSG, "User deleted successfully", LogKeys.USER_ID, id);
        return ResponseEntity.noContent().build();
    }
}
