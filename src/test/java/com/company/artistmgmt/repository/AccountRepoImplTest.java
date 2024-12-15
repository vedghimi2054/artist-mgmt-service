package com.company.artistmgmt.repository;

import com.company.artistmgmt.exception.RepoException;
import com.company.artistmgmt.helper.JsonHelper;
import com.company.artistmgmt.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AccountRepoImplTest {
    @Autowired
    private AccountRepo repo;

    @Test
    public void getAllUsers() throws RepoException {
        List<User> allUsers = repo.getAllUsers();
        String serialize = JsonHelper.serialize(allUsers);
        System.out.println("All users:" + serialize);
    }
}