package com.company.artistmgmt.repository;

import com.company.artistmgmt.exception.RepoException;
import com.company.artistmgmt.model.User;

import java.util.List;

public interface AccountRepo {
    List<User> getAllUsers() throws RepoException;
}
