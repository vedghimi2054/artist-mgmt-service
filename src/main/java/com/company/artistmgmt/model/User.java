package com.company.artistmgmt.model;

import com.company.artistmgmt.model.general.Gender;
import com.company.artistmgmt.model.general.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private LocalDateTime dob;
    private Gender gender;
    private String address;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

