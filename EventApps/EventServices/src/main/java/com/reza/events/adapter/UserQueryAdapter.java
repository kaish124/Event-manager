package com.reza.events.adapter;

import com.reza.events.domain.entity.User;
import com.reza.events.security.AuthenticatedUser;
import com.reza.events.repository.UserRepository;
import com.reza.events.port.UserQueryPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@Transactional(readOnly = true)
public class UserQueryAdapter implements UserQueryPort {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserQueryAdapter(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public AuthenticatedUser loadUser(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? mapper.map(user, AuthenticatedUser.class) : null;
    }

    @Override
    public AuthenticatedUser loadUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(user -> mapper.map(user, AuthenticatedUser.class)).orElse(null);
    }
}
