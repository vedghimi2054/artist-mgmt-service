package com.company.artistmgmt.service;

import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.repository.ArtistRepo;
import com.company.artistmgmt.repository.MusicRepo;
import com.company.artistmgmt.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.company.artistmgmt.util.MetaUtils.extractDashboardMeta;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final ArtistRepo artistRepo;
    private final UserRepo userRepo;
    private final MusicRepo musicRepo;

    public DashboardServiceImpl(ArtistRepo artistRepo, UserRepo userRepo, MusicRepo musicRepo) {
        this.artistRepo = artistRepo;
        this.userRepo = userRepo;
        this.musicRepo = musicRepo;
    }

    @Override
    public BaseResponse<List<Object>> getAllDashboardStats() throws ArtistException {
        logger.debug("Fetching dashboard statistics");

        long totalUserCount = userRepo.countTotalUsers();
        long totalArtistCount = artistRepo.getArtistCount();
        long totalAllSongs = musicRepo.countAllSongs();

        BaseResponse<List<Object>> response = new BaseResponse<>();
        return extractDashboardMeta(totalUserCount, totalArtistCount, totalAllSongs, response);
    }
}