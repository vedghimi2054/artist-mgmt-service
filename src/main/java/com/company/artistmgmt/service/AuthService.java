package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.LoginReqDto;
import com.company.artistmgmt.dto.LoginTokenDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    BaseResponse<LoginTokenDto> login(LoginReqDto loginReqDto, HttpServletRequest request) throws ArtistException;

}
