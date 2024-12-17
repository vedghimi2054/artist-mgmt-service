package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.UserDto;
import com.company.artistmgmt.model.general.Role;

import java.util.List;

public interface UserService {

    /**
     * Retrieves a paginated list of users.
     *
     * @param pageNo     the page number (0-indexed).
     * @param pageSize   the size of the page.
     * @param roleFilter optional filter for user role.
     * @return List of UserDto.
     */
    List<UserDto> getAllUsers(int pageNo, int pageSize, Role roleFilter);

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user.
     * @return the UserDto.
     */
    UserDto getUserById(int id);

    /**
     * Creates a new user.
     *
     * @param userDto the user details.
     */
    UserDto createUser(UserDto userDto);

    /**
     * Updates an existing user.
     *
     * @param id      the ID of the user to update.
     * @param userDto the updated user details.
     */
    UserDto updateUser(int id, UserDto userDto);

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete.
     */
    void deleteUser(int id);
}
