package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.LoginReqDto;
import com.company.artistmgmt.dto.LoginTokenDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.exception.NotAllowedException;
import com.company.artistmgmt.exception.ResourceNotFoundException;
import com.company.artistmgmt.helper.JsonHelper;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.model.User;
import com.company.artistmgmt.repository.UserRepo;
import com.company.artistmgmt.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final UserLoadServiceImpl userLoadService;
    private final UserRepo userRepository;

    @Override
    public BaseResponse<LoginTokenDto> login(LoginReqDto loginReqDto, HttpServletRequest request) throws ArtistException {
        logger.debug("Login payload:{}", JsonHelper.serialize(loginReqDto));
        User user = userRepository.findUserByEmail(loginReqDto.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        if (!bCryptPasswordEncoder.matches(loginReqDto.getPassword(), user.getPassword())) {
            throw new NotAllowedException("Invalid credentials");
        }
        HashMap<String, Object> additionalClaims = new HashMap<>();
        additionalClaims.put("user_id", user.getId());
        additionalClaims.put("email", user.getEmail());
        String token = jwtUtil.generateToken(userLoadService.loadUserByUsername(loginReqDto.getEmail()), additionalClaims);
        LoginTokenDto loginTokenDto = new LoginTokenDto();
        loginTokenDto.setUserId(user.getId());
        loginTokenDto.setToken(token);
        loginTokenDto.setRole(user.getRole());
        loginTokenDto.setFirstName(user.getFirstName());
        loginTokenDto.setLastName(user.getLastName());
        loginTokenDto.setEmail(user.getUsername());
        return new BaseResponse<>(true, loginTokenDto);

    }
}
