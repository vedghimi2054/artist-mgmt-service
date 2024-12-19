package com.company.artistmgmt.repository;

import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.User;

import java.util.List;

public interface UserRepo {
    List<User> getAllUsers(int pageNo, int pageSize) throws ArtistException;

    boolean createUser(User userEntity);

    boolean existsById(int id) throws ArtistException;

    boolean updateUser(int id, User useEntity);

    boolean deleteUser(int id);

    User getUserById(int id) throws ArtistException;

}
