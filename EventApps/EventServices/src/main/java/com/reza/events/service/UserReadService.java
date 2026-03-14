package com.reza.events.service;

import com.reza.events.dto.UserDto;

import java.util.List;

public interface UserReadService {
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto getUserByEmail(String email);
}
