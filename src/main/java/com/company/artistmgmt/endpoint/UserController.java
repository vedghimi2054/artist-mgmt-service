package com.company.artistmgmt.endpoint;

import com.company.artistmgmt.dto.UserDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * List all users with pagination. Only accessible to SUPER_ADMIN.
     *
     * @param pageNo   the page number (default: 0)
     * @param pageSize the number of records per page (default: 10)
     * @return paginated list of users
     */
    @GetMapping
    public ResponseEntity<BaseResponse<List<UserDto>>> listUsers(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            BaseResponse<List<UserDto>> users = userService.getAllUsers(pageNo, pageSize);
            return ResponseEntity.ok(users);
        } catch (ArtistException ex) {
            BaseResponse<List<UserDto>> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Create a new user. Only accessible to SUPER_ADMIN.
     *
     * @param userDto the user DTO
     * @return the created user
     */
    @PostMapping
    public ResponseEntity<BaseResponse<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            BaseResponse<UserDto> createdUser = userService.createUser(userDto);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (ArtistException ex) {
            BaseResponse<UserDto> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update an existing user. Only accessible to SUPER_ADMIN.
     *
     * @param id      the user ID
     * @param userDto the updated user data
     * @return the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<UserDto>> updateUser(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
        try {
            BaseResponse<UserDto> updatedUser = userService.updateUser(id, userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (ArtistException ex) {
            BaseResponse<UserDto> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Delete a user by ID. Only accessible to SUPER_ADMIN.
     *
     * @param id the user ID
     * @return a success response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Integer>> deleteUser(@PathVariable int id) {
        try {
            BaseResponse<Integer> deletedId = userService.deleteUser(id);
            return ResponseEntity.ok(deletedId);

        } catch (ArtistException ex) {
            BaseResponse<Integer> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Get a user by ID. Accessible to SUPER_ADMIN only.
     *
     * @param id the user ID
     * @return the user details
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<UserDto>> getUserById(@PathVariable int id) {
        try {
            BaseResponse<UserDto> user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (ArtistException ex) {
            BaseResponse<UserDto> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }
}