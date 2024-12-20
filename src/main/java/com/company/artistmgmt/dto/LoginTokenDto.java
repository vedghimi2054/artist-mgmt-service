package com.company.artistmgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenDto {

    private int userId;
    private String email;
    private String token;
    private String firstName;
    private String lastName;

}
