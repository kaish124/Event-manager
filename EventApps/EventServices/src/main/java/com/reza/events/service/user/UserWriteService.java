package com.reza.events.service.user;

import com.reza.events.dto.UserDto;

/**
 * Service interface for user write operations.
 * Provides methods for creating, updating, and deleting users.
 */
public interface UserWriteService {
    
    /**
     * Creates a new user in the system.
     *
     * @param userDto the user data transfer object containing user information
     * @return the created user as a UserDto
     */
    UserDto createUser(UserDto userDto);
    
    /**
     * Updates an existing user's information.
     *
     * @param id the unique identifier of the user to update
     * @param userDto the updated user data
     * @return the updated user as a UserDto
     */
    UserDto updateUser(Long id, UserDto userDto);
    
    /**
     * Deletes a user from the system.
     *
     * @param id the unique identifier of the user to delete
     */
    void deleteUser(Long id);
}
