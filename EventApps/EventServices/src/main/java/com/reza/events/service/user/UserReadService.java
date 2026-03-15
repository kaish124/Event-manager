package com.reza.events.service.user;

import com.reza.events.dto.UserDto;

import java.util.List;

/**
 * Service interface for user read operations.
 * Provides methods to retrieve user information from the system.
 */
public interface UserReadService {
    
    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve
     * @return the UserDto containing user information
     */
    UserDto getUserById(Long id);
    
    /**
     * Retrieves all users in the system.
     *
     * @return a list of UserDto objects containing all users
     */
    List<UserDto> getAllUsers();
    
    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve
     * @return the UserDto containing user information
     */
    UserDto getUserByEmail(String email);
}
