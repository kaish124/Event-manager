package com.reza.events.service.user.impl;

import com.reza.events.domain.entity.User;
import com.reza.events.dto.UserDto;
import com.reza.events.exception.ResourceNotFoundException;
import com.reza.events.logging.LogKeys;
import com.reza.events.logging.Logger;
import com.reza.events.logging.LoggerFactory;
import com.reza.events.repository.UserRepository;
import com.reza.events.service.user.UserReadService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserReadServiceImpl implements UserReadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserReadServiceImpl.class);
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto getUserById(Long id) {
        LOGGER.debug(LogKeys.MSG, "Getting user by ID", LogKeys.USER_ID, id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        LOGGER.info(LogKeys.MSG, "Found user", LogKeys.USER_ID, id);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        LOGGER.debug(LogKeys.MSG, "Getting all users");
        List<User> users = userRepository.findAll();
        LOGGER.info(LogKeys.MSG, "Found users", "COUNT", users.size());
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        LOGGER.debug(LogKeys.MSG, "Getting user by email", LogKeys.EMAIL, email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            LOGGER.warn(LogKeys.MSG, "User not found", LogKeys.EMAIL, email);
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        LOGGER.info(LogKeys.MSG, "Found user", LogKeys.EMAIL, email);
        return modelMapper.map(user, UserDto.class);
    }
}