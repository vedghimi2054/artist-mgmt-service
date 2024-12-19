package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.UserDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.BaseResponse;

import java.util.List;

public interface UserService {

    /**
     * Retrieves a paginated list of users.
     *
     * @param pageNo   the page number (0-indexed).
     * @param pageSize the size of the page.
     * @return List of UserDto.
     */
    BaseResponse<List<UserDto>> getAllUsers(int pageNo, int pageSize) throws ArtistException;

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user.
     * @return the UserDto.
     */
    BaseResponse<UserDto> getUserById(int id) throws ArtistException;

    /**
     * Creates a new user.
     *
     * @param userDto the user details.
     */
    BaseResponse<UserDto> createUser(UserDto userDto) throws ArtistException;

    /**
     * Updates an existing user.
     *
     * @param id      the ID of the user to update.
     * @param userDto the updated user details.
     */
    BaseResponse<UserDto> updateUser(int id, UserDto userDto) throws ArtistException;

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete.
     */
    BaseResponse<Integer> deleteUser(int id) throws ArtistException;

}
