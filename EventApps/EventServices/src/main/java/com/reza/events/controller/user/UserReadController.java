package com.reza.events.controller.user;

import com.reza.events.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller interface for user read operations.
 * Provides endpoints for retrieving user information.
 * All endpoints require VIEW_USER permission.
 */
@RequestMapping("/services/user")
public interface UserReadController {

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve
     * @return ResponseEntity containing the UserDto with HTTP status 200 (OK)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'VIEW_USER')")
    ResponseEntity<UserDto> getUserById(@PathVariable Long id);

    /**
     * Retrieves all users in the system.
     *
     * @return ResponseEntity containing a list of UserDto objects with HTTP status 200 (OK)
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'VIEW_USER')")
    ResponseEntity<List<UserDto>> getAllUsers();

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve
     * @return ResponseEntity containing the UserDto with HTTP status 200 (OK)
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasPermission(null, 'VIEW_USER')")
    ResponseEntity<UserDto> getUserByEmail(@PathVariable String email);
}