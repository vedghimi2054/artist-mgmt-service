package com.company.artistmgmt.mapper;

import com.company.artistmgmt.dto.UserReqDto;
import com.company.artistmgmt.dto.UserResDto;
import com.company.artistmgmt.model.User;

public class UserMapper {
    public static UserResDto toUserDto(User user) {
        UserResDto dto = new UserResDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setDob(user.getDob());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public static User toUserEntity(UserReqDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setDob(dto.getDob());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());
        user.setRole(dto.getRole());
        user.setPassword(dto.getPassword());
        return user;
    }
}
