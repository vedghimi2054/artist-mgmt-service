package com.company.artistmgmt.repository;

import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.User;

import java.util.List;

public interface UserRepo {
    List<User> getAllUsers(int validatedPageSize, int offset) throws ArtistException;

    User createUser(User userEntity) throws ArtistException;

    boolean existsById(int id) throws ArtistException;

    boolean updateUser(int id, User useEntity);

    boolean deleteUser(int id);

    User getUserById(int id) throws ArtistException;

    long countTotalUsers() throws ArtistException;

    User findUserByEmail(String email) throws ArtistException;
    User findUserByPhone(String email) throws ArtistException;
}
