package com.company.artistmgmt.dto;

import com.company.artistmgmt.model.general.Gender;
import com.company.artistmgmt.model.general.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResDto {
    private int id;
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;
    private LocalDateTime dob;
    private Gender gender;
    @NotBlank(message = "Address is required")
    private String address;
    @NotNull(message = "Role is required")
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}