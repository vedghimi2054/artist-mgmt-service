package com.example.artistmanagement.controller;

import com.company.artistmgmt.dto.UserDto;
import com.company.artistmgmt.model.general.Role;
import com.company.artistmgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * List all users with pagination. Only accessible to SUPER_ADMIN.
     *
     * @param pageNo   the page number (default: 0)
     * @param pageSize the number of records per page (default: 10)
     * @return paginated list of users
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> listUsers(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Role roleFilter) {
        List<UserDto> users = userService.getAllUsers(pageNo, pageSize, roleFilter);
        return ResponseEntity.ok(users);
    }

    /**
     * Create a new user. Only accessible to SUPER_ADMIN.
     *
     * @param userDto the user DTO
     * @return the created user
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Update an existing user. Only accessible to SUPER_ADMIN.
     *
     * @param id      the user ID
     * @param userDto the updated user data
     * @return the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete a user by ID. Only accessible to SUPER_ADMIN.
     *
     * @param id the user ID
     * @return a success response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a user by ID. Accessible to SUPER_ADMIN only.
     *
     * @param id the user ID
     * @return the user details
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable int id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
