package com.reza.events.service.user.impl;

import com.reza.events.domain.entity.Role;
import com.reza.events.domain.entity.User;
import com.reza.events.domain.entity.UserRole;
import com.reza.events.dto.RoleBean;
import com.reza.events.dto.UserDto;
import com.reza.events.exception.ResourceNotFoundException;
import com.reza.events.logging.LogKeys;
import com.reza.events.logging.Logger;
import com.reza.events.logging.LoggerFactory;
import com.reza.events.repository.UserRepository;
import com.reza.events.service.user.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWriteServiceImpl implements UserWriteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserWriteServiceImpl.class);
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        LOGGER.debug(LogKeys.MSG, "Creating user", LogKeys.EMAIL, userDto.getEmail());
        try {
            User user = modelMapper.map(userDto, User.class);
            
            // Ensure bidirectional relationship is properly set
            if (user.getUserRoles() != null) {
                for (UserRole userRole : user.getUserRoles()) {
                    userRole.setUser(user);
                }
            }
            
            User savedUser = userRepository.save(user);
            LOGGER.info(LogKeys.MSG, "User created successfully", LogKeys.USER_ID, savedUser.getId(), LogKeys.EMAIL, savedUser.getEmail());
            return modelMapper.map(savedUser, UserDto.class);
        } catch (Exception e) {
            LOGGER.error(e, LogKeys.MSG, "Error creating user", LogKeys.EMAIL, userDto.getEmail());
            throw e;
        }
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        LOGGER.debug(LogKeys.MSG, "Updating user", LogKeys.USER_ID, id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
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
        LOGGER.info(LogKeys.MSG, "User updated successfully", LogKeys.USER_ID, id);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Long id) {
        LOGGER.debug(LogKeys.MSG, "Deleting user", LogKeys.USER_ID, id);
        if (!userRepository.existsById(id)) {
            LOGGER.warn(LogKeys.MSG, "User not found for deletion", LogKeys.USER_ID, id);
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        LOGGER.info(LogKeys.MSG, "User deleted successfully", LogKeys.USER_ID, id);
    }
}