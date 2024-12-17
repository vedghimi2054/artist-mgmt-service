package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.UserDto;
import com.company.artistmgmt.exception.ResourceNotFoundException;
import com.company.artistmgmt.exception.ValidationException;
import com.company.artistmgmt.model.general.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers(int pageNo, int pageSize, Role roleFilter) {
        validatePagination(pageNo, pageSize);
        return userRepository.getAllUsers(pageNo, pageSize, roleFilter);
    }

    @Override
    public UserDto getUserById(int id) {
        validateId(id);
        return userRepository.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        validateUserDto(userDto);
        userRepository.createUser(userDto);
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto) {
        validateId(id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }

        validateUserDto(userDto);
        userRepository.updateUser(id, userDto);
    }

    @Override
    public void deleteUser(int id) {
        validateId(id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }

        userRepository.deleteUser(id);
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
     * Validates pagination parameters.
     */
    private void validatePagination(int pageNo, int pageSize) {
        if (pageNo < 0) {
            throw new ValidationException("Page number cannot be negative");
        }
        if (pageSize <= 0) {
            throw new ValidationException("Page size must be greater than zero");
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