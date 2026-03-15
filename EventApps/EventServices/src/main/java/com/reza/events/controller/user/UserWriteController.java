package com.reza.events.controller.user;

import com.reza.events.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller interface for user write operations.
 * Provides endpoints for creating, updating, and deleting users.
 * All endpoints require MANAGE_USER permission.
 */
@RequestMapping("/services/user")
public interface UserWriteController {
    
    /**
     * Creates a new user in the system.
     *
     * @param userDto the user data transfer object containing user information
     * @return ResponseEntity containing the created UserDto with HTTP status 201 (Created)
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_USER')")
    ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto);
    
    /**
     * Updates an existing user's information.
     *
     * @param id the unique identifier of the user to update
     * @param userDto the updated user data
     * @return ResponseEntity containing the updated UserDto with HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_USER')")
    ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto);
    
    /**
     * Deletes a user from the system.
     *
     * @param id the unique identifier of the user to delete
     * @return ResponseEntity with no content and HTTP status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_USER')")
    ResponseEntity<Void> deleteUser(@PathVariable Long id);
}