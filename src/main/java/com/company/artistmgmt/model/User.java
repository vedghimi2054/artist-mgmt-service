package com.company.artistmgmt.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private LocalDateTime dob;
    private Integer gender;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
