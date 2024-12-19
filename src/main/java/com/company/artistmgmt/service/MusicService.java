package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.MusicDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.BaseResponse;

import java.util.List;

public interface MusicService {

    /**
     * Retrieves a paginated list of music for a specific artist.
     *
     * @param artistId the ID of the artist.
     * @param pageNo   the page number (0-indexed).
     * @param pageSize the size of the page.
     * @return List of musicDto.
     */
    BaseResponse<List<MusicDto>> getSongsByArtist(int artistId, int pageNo, int pageSize) throws ArtistException;
    /**
     * Creates a new song.
     *
     * @param musicDto the details of the new song.
     * @return the created musicDto.
     */
    BaseResponse<MusicDto> createSongForArtist(int artistId, MusicDto musicDto) throws ArtistException;

    /**
     * Updates an existing song.
     *
     * @param id       the ID of the song to update.
     * @param musicDto the updated details of the song.
     * @return the updated musicDto.
     */
    BaseResponse<MusicDto> updateSongForArtist(int artistId, int id, MusicDto musicDto) throws ArtistException;

    /**
     * Deletes a song by ID.
     *
     * @param id the ID of the song to delete.
     * @return the ID of the deleted song.
     */
    BaseResponse<Integer> deleteSongForArtist(int artistId, int id) throws ArtistException;
}
