package com.company.artistmgmt.dto;

import com.company.artistmgmt.model.general.Gender;
import com.company.artistmgmt.model.general.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserReqDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime dob;
    private Gender gender;
    private String address;
    private Role role;
    private String password;
}