package com.reza.events.service.user.impl;

import com.reza.events.domain.entity.User;
import com.reza.events.dto.UserDto;
import com.reza.events.exception.ResourceNotFoundException;
import com.reza.events.repository.UserRepository;
import com.reza.events.service.user.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserReadServiceImpl implements UserReadService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto getUserById(Long id) {
        log.debug("Getting user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        log.info("Found user with id: {}", id);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.debug("Getting all users");
        List<User> users = userRepository.findAll();
        log.info("Found {} users", users.size());
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        log.debug("Getting user by email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("User not found with email: {}", email);
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        log.info("Found user with email: {}", email);
        return modelMapper.map(user, UserDto.class);
    }
}
