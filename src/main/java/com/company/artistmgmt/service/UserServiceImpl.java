package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.LoginReqDto;
import com.company.artistmgmt.dto.LoginTokenDto;
import com.company.artistmgmt.dto.UserReqDto;
import com.company.artistmgmt.dto.UserResDto;
import com.company.artistmgmt.exception.*;
import com.company.artistmgmt.helper.JsonHelper;
import com.company.artistmgmt.mapper.UserStructMapper;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.model.User;
import com.company.artistmgmt.model.general.Gender;
import com.company.artistmgmt.model.general.Role;
import com.company.artistmgmt.repository.UserRepo;
import com.company.artistmgmt.security.JwtUtil;
import com.company.artistmgmt.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.artistmgmt.repository.QueryConst.*;
import static com.company.artistmgmt.util.MetaUtils.extractedMeta;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserStructMapper userMapper;
    private final UserRepo userRepository;

    @Override
    public BaseResponse<UserResDto> createUser(UserReqDto userReqDto) throws ArtistException {
        logger.debug("Creating user with Payload:{}", userReqDto);
        validateUserDto(userReqDto);
        User userEntity = userMapper.toEntityFromReq(userReqDto);
        String email = userReqDto.getEmail();
        User userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail != null) {
            throw new FailedException("Email " + email + " already taken. Please choose a different email.");
        }
        userEntity.setEmail(email);
        userEntity.setPassword(bCryptPasswordEncoder.encode(userReqDto.getPassword()));
        User user = userRepository.createUser(userEntity);

        User userById = userRepository.getUserById(user.getId());
        UserResDto userResDto = userMapper.toDto(userById);
        return new BaseResponse<>(true, userResDto);
    }

    @Override
    public BaseResponse<UserResDto> updateUser(int userId, UserReqDto userReqDto) throws ArtistException {
        logger.debug("Updating user with id:{} and Payload:{}", userId, userReqDto);
        validateId(userId);
        validateUserDto(userReqDto);
        User existingUser = userRepository.getUserById(userId);
        if (existingUser == null) {
            throw new ResourceNotFoundException("User with id " + userId + " not found.");
        }
        userMapper.updateUserFromDto(userReqDto, existingUser);
        userMapper.updatePassword(existingUser, userReqDto.getPassword());

        if (!existingUser.getRole().equals(userReqDto.getRole())) {
            if (!UserUtil.isSuperAdmin()) {
                throw new NotAllowedException("Only super admin can update the role");
            }
            existingUser.setRole(userReqDto.getRole());
        }
        if (!userRepository.updateUser(userId, existingUser)) {
            throw new FailedException("Failed to update user.");
        }
        User userById = userRepository.getUserById(userId);
        UserResDto userResDto = userMapper.toDto(userById);
        return new BaseResponse<>(true, userResDto);

    }

    @Override
    public BaseResponse<Integer> deleteUser(int userId) throws ArtistException {
        logger.debug("Deleting user with id:{}", userId);
        validateId(userId);

        if (userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }
        if (!userRepository.deleteUser(userId)) {
            throw new FailedException("Failed to update user.");
        }
        return new BaseResponse<>(true, userId);
    }

    @Override
    public BaseResponse<List<UserResDto>> getAllUsers(int pageNo, int pageSize) throws ArtistException {
        logger.debug("Getting all user Payload:{}", pageNo);
        // Validate and set default pagination values
        if (pageNo < 0 || pageSize <= 0) {
            throw new ValidationException("pageNo must be >= 0 and pageSize must be > 0");
        }
        // Calculate offset
        int offset = pageNo * pageSize;
        List<User> allUsers = userRepository.getAllUsers(pageSize, offset);
        long totalCount = userRepository.countTotalUsers();
        List<UserResDto> userDtoList = allUsers.stream().map(userMapper::toDto).collect(Collectors.toList());
        BaseResponse<List<UserResDto>> response = new BaseResponse<>();
        response.setMessage(USER_SUCCESS_MSG);
        response.setDataResponse(userDtoList);
        extractedMeta(pageNo, pageSize, response, totalCount);
        return response;
    }

    @Override
    public BaseResponse<UserResDto> getUserById(int id) throws ArtistException {
        logger.debug("Getting user with id:{}", id);
        validateId(id);
        if (userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }
        User userById = userRepository.getUserById(id);
        UserResDto userResDto = userMapper.toDto(userById);
        return new BaseResponse<>(true, userResDto);
    }

    /**
     * Validates the user ID.
     */
    private void validateId(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid user ID");
        }
    }

    /**
     * Validates the UserReqDto object.
     */
    private void validateUserDto(UserReqDto userReqDto) {
        if (userReqDto.getFirstName() == null || userReqDto.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name cannot be null or empty");
        }

        if (userReqDto.getLastName() == null || userReqDto.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name cannot be null or empty");
        }

        if (userReqDto.getEmail() == null || !userReqDto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format");
        }

        if (userReqDto.getPhone() == null || !userReqDto.getPhone().matches("^[0-9]{10}$")) {
            throw new ValidationException("Phone number must be 10 digits");
        }

        if (userReqDto.getRole() == null || !Role.isValid(userReqDto.getRole().name())) {
            throw new ValidationException("Invalid role value");
        }

        if (userReqDto.getGender() != null && Gender.isValid(userReqDto.getGender().name())) {
            throw new ValidationException("Invalid gender value");
        }
    }
}