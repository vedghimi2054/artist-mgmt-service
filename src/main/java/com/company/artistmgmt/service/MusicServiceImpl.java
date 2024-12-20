package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.ArtistDto;
import com.company.artistmgmt.dto.MusicDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.exception.FailedException;
import com.company.artistmgmt.exception.ResourceNotFoundException;
import com.company.artistmgmt.exception.ValidationException;
import com.company.artistmgmt.mapper.MusicMapper;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.model.Music;
import com.company.artistmgmt.repository.ArtistRepo;
import com.company.artistmgmt.repository.MusicRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.artistmgmt.mapper.MusicMapper.toMusicDto;
import static com.company.artistmgmt.mapper.MusicMapper.toMusicEntity;
import static com.company.artistmgmt.util.PaginationUtils.validateAndSetDefaults;

@Service
public class MusicServiceImpl implements MusicService {
    private static final Logger logger = LoggerFactory.getLogger(MusicServiceImpl.class);

    private final MusicRepo musicRepo;
    private final ArtistRepo artistRepo;

    public MusicServiceImpl(MusicRepo musicRepo, ArtistRepo artistRepo) {
        this.musicRepo = musicRepo;
        this.artistRepo = artistRepo;
    }

    @Override
    public BaseResponse<List<MusicDto>> getSongsByArtist(int artistId, int pageNo, int pageSize) throws ArtistException {
        logger.debug("Getting songs by artist with: Payload:{}", artistId);
        // Validate and set default pagination values
        if (pageNo < 0 || pageSize <= 0) {
            throw new ValidationException("pageNo must be >= 0 and pageSize must be > 0");
        }
        // Calculate offset
        int offset = pageNo * pageSize;
        if (!artistRepo.checkArtistExistsById(artistId)) {
            throw new ResourceNotFoundException("Artist ID " + artistId + " not found.");
        }
        List<Music> musicEntities = musicRepo.getSongsByArtist(artistId, pageSize, offset);
        var totalCount = musicRepo.countSongsByArtist(artistId);
        List<MusicDto> musicDtoList = musicEntities.stream().map(MusicMapper::toMusicDto).collect(Collectors.toList());
        // Add pagination details to response
        BaseResponse<List<MusicDto>> response = new BaseResponse<>();
        response.setSuccess(true);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage("Music Fetch successfully");
        response.setStatusCode(HttpStatus.OK.value());
        response.setDataResponse(musicDtoList);
        response.addMeta("totalCount", totalCount);
        response.addMeta("totalPages", (int) Math.ceil((double) totalCount / pageSize));
        response.addMeta("currentPage", pageNo);
        response.addMeta("pageSize", pageSize);
        return response;
    }

    @Override
    public BaseResponse<MusicDto> createSongForArtist(int artistId, MusicDto musicDto) throws ArtistException {
        logger.debug("Creating songs for artist id:{} with: Payload:{}", artistId, musicDto);
        Music musicEntity = toMusicEntity(musicDto);
        if (!artistRepo.checkArtistExistsById(artistId)) {
            throw new ResourceNotFoundException("Artist ID " + artistId + " not found for artist.");
        }
        Music songForArtist = musicRepo.createSongForArtist(artistId, musicEntity);

        Music musicById = musicRepo.getSongById(songForArtist.getId(), artistId);
        MusicDto updatedDTo = toMusicDto(musicById);
        return new BaseResponse<>(true, updatedDTo);
    }

    @Override
    public BaseResponse<MusicDto> updateSongForArtist(int artistId, int id, MusicDto musicDto) throws ArtistException {
        logger.debug("Updating songs for artist id:{} and musicId:{} with: Payload:{}", artistId, id, musicDto);
        Music musicEntity = toMusicEntity(musicDto);
        if (!musicRepo.checkMusicExistsById(id, artistId)) {
            throw new ResourceNotFoundException("Music with ID " + id + " not found for artist.");
        }
        if (!musicRepo.updateSongForArtist(artistId, id, musicEntity)) {
            throw new FailedException("Failed to update Song for artist.");
        }
        Music updatedSong = musicRepo.getSongById(id, artistId);
        MusicDto dto = toMusicDto(updatedSong);
        return new BaseResponse<>(true, dto);
    }

    @Override
    public BaseResponse<Integer> deleteSongForArtist(int artistId, int id) throws ArtistException {
        logger.debug("Deleting songs for artist id:{} and musicId:{}", artistId, id);
        if (!musicRepo.checkMusicExistsById(id, artistId)) {
            throw new ResourceNotFoundException("Music with ID " + id + " not found for artist");
        }
        if (!musicRepo.deleteSongForArtist(artistId, id)) {
            throw new FailedException("Failed to delete song for artist.");
        }
        return new BaseResponse<>(true, id);
    }
}
