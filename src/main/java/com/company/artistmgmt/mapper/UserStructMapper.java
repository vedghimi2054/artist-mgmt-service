package com.company.artistmgmt.mapper;

import com.company.artistmgmt.dto.UserReqDto;
import com.company.artistmgmt.dto.UserResDto;
import com.company.artistmgmt.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserStructMapper {

    UserStructMapper INSTANCE = Mappers.getMapper(UserStructMapper.class);

    @Mapping(target = "password", ignore = true)
    User toEntityFromReq(UserReqDto userReqDto);

    @Mapping(target = "password", ignore = true)
    User toEntityFromRes(UserResDto userResDto);

    UserResDto toDto(User user);


    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUserFromDto(UserReqDto userReqDto, @MappingTarget User existingUser);

    default void updatePassword(@MappingTarget User existingUser, String password) {
        if (password != null && !password.isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            existingUser.setPassword(passwordEncoder.encode(password));
        }
    }

}
