package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.LoginReqDto;
import com.company.artistmgmt.dto.LoginTokenDto;
import com.company.artistmgmt.dto.UserReqDto;
import com.company.artistmgmt.dto.UserResDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {

    /**
     * Retrieves a paginated list of users.
     *
     * @param pageNo   the page number (0-indexed).
     * @param pageSize the size of the page.
     * @return List of UserReqDto.
     */
    BaseResponse<List<UserResDto>> getAllUsers(int pageNo, int pageSize) throws ArtistException;

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user.
     * @return the UserReqDto.
     */
    BaseResponse<UserResDto> getUserById(int id) throws ArtistException;

    /**
     * Creates a new user.
     *
     * @param userDto the user details.
     */
    BaseResponse<UserResDto> createUser(UserReqDto userDto) throws ArtistException;

    /**
     * Updates an existing user.
     *
     * @param id      the ID of the user to update.
     * @param userDto the updated user details.
     */
    BaseResponse<UserResDto> updateUser(int id, UserReqDto userDto) throws ArtistException;

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete.
     */
    BaseResponse<Integer> deleteUser(int id) throws ArtistException;

    BaseResponse<LoginTokenDto> login(LoginReqDto loginReqDto, HttpServletRequest request) throws ArtistException;
}
