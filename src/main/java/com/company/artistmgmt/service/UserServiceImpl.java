package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.UserDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.exception.FailedException;
import com.company.artistmgmt.exception.ResourceNotFoundException;
import com.company.artistmgmt.exception.ValidationException;
import com.company.artistmgmt.mapper.UserMapper;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.model.User;
import com.company.artistmgmt.model.general.Gender;
import com.company.artistmgmt.model.general.Role;
import com.company.artistmgmt.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.company.artistmgmt.mapper.UserMapper.toUserDto;
import static com.company.artistmgmt.mapper.UserMapper.toUserEntity;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepository;

    public UserServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BaseResponse<UserDto> createUser(UserDto userDto) throws ArtistException {
        validateUserDto(userDto);
        User userEntity = toUserEntity(userDto);
        if (!userRepository.createUser(userEntity)) {
            throw new FailedException("Failed to create user.");
        }
        User userById = userRepository.getUserById(userEntity.getId());
        UserDto userDto1 = toUserDto(userById);
        return new BaseResponse<>(true, userDto1);
    }

    @Override
    public BaseResponse<UserDto> updateUser(int id, UserDto userDto) throws ArtistException {
        validateId(id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }

        validateUserDto(userDto);
        User userEntity = toUserEntity(userDto);
        if (!userRepository.updateUser(id, userEntity)) {
            throw new FailedException("Failed to update user.");
        }
        User userById = userRepository.getUserById(id);
        UserDto userDto1 = toUserDto(userById);
        return new BaseResponse<>(true, userDto1);

    }

    @Override
    public BaseResponse<Integer> deleteUser(int userId) throws ArtistException {
        validateId(userId);

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }
        if (!userRepository.deleteUser(userId)) {
            throw new FailedException("Failed to update user.");
        }
        return new BaseResponse<>(true, userId);
    }

    @Override
    public BaseResponse<List<UserDto>> getAllUsers(int pageNo, int pageSize) throws ArtistException {
        List<User> allUsers = userRepository.getAllUsers(pageNo, pageSize);
        List<UserDto> collect = allUsers.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        return new BaseResponse<>(true, collect);
    }

    @Override
    public BaseResponse<UserDto> getUserById(int id) throws ArtistException {
        validateId(id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }
        User userById = userRepository.getUserById(id);
        UserDto userDto = toUserDto(userById);
        return new BaseResponse<>(true, userDto);
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
     * Validates the UserDto object.
     */
    private void validateUserDto(UserDto userDto) {
        if (userDto.getFirstName() == null || userDto.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name cannot be null or empty");
        }

        if (userDto.getLastName() == null || userDto.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name cannot be null or empty");
        }

        if (userDto.getEmail() == null || !userDto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format");
        }

        if (userDto.getPhone() == null || !userDto.getPhone().matches("^[0-9]{10}$")) {
            throw new ValidationException("Phone number must be 10 digits");
        }

        if (userDto.getRole() == null || !Role.isValid(userDto.getRole().name())) {
            throw new ValidationException("Invalid role value");
        }

        if (userDto.getGender() != null && !Gender.isValid(userDto.getGender().name())) {
            throw new ValidationException("Invalid gender value");
        }
    }
}