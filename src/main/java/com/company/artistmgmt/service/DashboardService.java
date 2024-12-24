package com.company.artistmgmt.service;

import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.BaseResponse;

import java.util.List;

public interface DashboardService {


    BaseResponse<List<Object>> getAllDashboardStats() throws ArtistException;
}
