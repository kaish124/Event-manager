package com.reza.events.service;

import com.reza.events.dto.UserDto;

public interface UserWriteService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
}
