package com.reza.events.service.user.impl;

import com.reza.events.domain.entity.Role;
import com.reza.events.domain.entity.User;
import com.reza.events.domain.entity.UserRole;
import com.reza.events.dto.RoleBean;
import com.reza.events.dto.UserDto;
import com.reza.events.repository.UserRepository;
import com.reza.events.service.user.UserWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserWriteServiceImpl implements UserWriteService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        log.debug("Creating user with email: {}", userDto.getEmail());
        try {
            User user = modelMapper.map(userDto, User.class);
            
            // Ensure bidirectional relationship is properly set
            if (user.getUserRoles() != null) {
                for (UserRole userRole : user.getUserRoles()) {
                    userRole.setUser(user);
                }
            }
            
            User savedUser = userRepository.save(user);
            log.info("User created successfully with id: {}", savedUser.getId());
            return modelMapper.map(savedUser, UserDto.class);
        } catch (Exception e) {
            log.error("Error creating user with email: {}", userDto.getEmail(), e);
            throw e;
        }
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        log.debug("Updating user with id: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Update basic fields
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(userDto.getPassword());
        
        // Update roles if provided
        if (userDto.getRoles() != null) {
            // Clear existing roles using orphan removal
            existingUser.getUserRoles().clear();
            
            // Add new roles
            for (RoleBean roleBean : userDto.getRoles()) {
                UserRole userRole = new UserRole();
                
                // Map RoleBean to Role
                Role role = new Role();
                role.setId(roleBean.getId());
                role.setName(roleBean.getName());
                role.setDescription(roleBean.getDescription());
                role.setType(roleBean.getType());
                
                userRole.setRole(role);
                userRole.setId(roleBean.getUserRoleId());
                existingUser.addUserRole(userRole);
            }
        }
        
        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with id: {}", id);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("User not found for deletion with id: {}", id);
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }
}
