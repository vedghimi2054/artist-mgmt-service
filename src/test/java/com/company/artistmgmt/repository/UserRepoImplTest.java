package com.company.artistmgmt.repository;

import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.helper.JsonHelper;
import com.company.artistmgmt.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserRepoImplTest {
    @Autowired
    private UserRepo repo;

    @Test
    public void getAllUsers() throws ArtistException {
        List<User> allUsers = repo.getAllUsers(1, 3);
        String serialize = JsonHelper.serialize(allUsers);
        System.out.println("All users:" + serialize);
    }
}