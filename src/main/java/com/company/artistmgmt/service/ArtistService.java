package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.ArtistDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.Artist;
import com.company.artistmgmt.model.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArtistService {

    /**
     * Retrieves a paginated list of artist.
     *
     * @param pageNo   the page number (0-indexed).
     * @param pageSize the size of the page.
     * @return List of artistDto.
     */
    BaseResponse<List<ArtistDto>> getAllArtists(int pageNo, int pageSize) throws ArtistException;

    /**
     * Retrieves a artist by ID.
     *
     * @param id the ID of the artist.
     * @return the artistDto.
     */
    BaseResponse<ArtistDto> getArtistById(int id) throws ArtistException;

    /**
     * Creates a new artist.
     *
     * @param artistDto the artist details.
     */
    BaseResponse<ArtistDto> createArtist(ArtistDto artistDto) throws ArtistException;

    /**
     * Updates an existing artist.
     *
     * @param id        the ID of the artist to update.
     * @param artistDto the updated artist details.
     */
    BaseResponse<ArtistDto> updateArtist(int id, ArtistDto artistDto) throws ArtistException;

    /**
     * Deletes a artist by ID.
     *
     * @param id the ID of the artist to delete.
     */
    BaseResponse<Integer> deleteArtist(int id) throws ArtistException;

    BaseResponse<List<ArtistDto>> importArtistsFromCsv(MultipartFile file) throws ArtistException;

    String generateArtistsCsvContent(List<Artist> artists) throws IOException;
}
