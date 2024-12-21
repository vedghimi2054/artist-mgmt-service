package com.company.artistmgmt.endpoint;

import com.company.artistmgmt.dto.LoginReqDto;
import com.company.artistmgmt.dto.LoginTokenDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.exception.AuthException;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginTokenDto>> login(
            @RequestBody LoginReqDto loginReqDto,
            HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginReqDto.getEmail(), loginReqDto.getPassword())
            );
            if (authentication.isAuthenticated()) {
                BaseResponse<LoginTokenDto> loginTokenDTO = authService.login(loginReqDto, request);
                return ResponseEntity.ok(loginTokenDTO);
            } else {
                throw new AuthException("Authentication failed.");
            }
        } catch (UsernameNotFoundException | ArtistException ex) {
            BaseResponse<LoginTokenDto> errorResponse = new BaseResponse<>(
                    HttpStatus.UNAUTHORIZED.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

    }
}
