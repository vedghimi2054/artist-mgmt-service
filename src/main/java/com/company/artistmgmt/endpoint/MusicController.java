package com.company.artistmgmt.endpoint;

import com.company.artistmgmt.dto.MusicDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.service.MusicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/music")
@Validated
public class MusicController {
    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * List all songs for a particular artist. Accessible to SUPER_ADMIN, ARTIST_MANAGER, and ARTIST.
     *
     * @param artistId the artist's ID
     * @param pageNo   the page number (default: 0)
     * @param pageSize the number of records per page (default: 10)
     * @return paginated list of songs for the given artist
     */
    @GetMapping("/artist/{artistId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ARTIST_MANAGER')")
    public ResponseEntity<BaseResponse<List<MusicDto>>> listSongsForArtist(
            @PathVariable int artistId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            BaseResponse<List<MusicDto>> songs = musicService.getSongsByArtist(artistId, pageNo, pageSize);
            return ResponseEntity.ok(songs);
        } catch (ArtistException ex) {
            BaseResponse<List<MusicDto>> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Create a new song for the artist. Accessible to ARTIST.
     *
     * @param artistId the artist's ID
     * @param musicDto the music DTO
     * @return the created music
     */
    @PostMapping("/artist/{artistId}")
//    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ARTIST')")
    public ResponseEntity<BaseResponse<MusicDto>> createSongForArtist(
            @PathVariable int artistId, @Valid @RequestBody MusicDto musicDto) {
        try {
            BaseResponse<MusicDto> createdSong = musicService.createSongForArtist(artistId, musicDto);
            return new ResponseEntity<>(createdSong, HttpStatus.CREATED);
        } catch (ArtistException ex) {
            BaseResponse<MusicDto> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update an existing song for the artist. Accessible to ARTIST.
     *
     * @param artistId the artist's ID
     * @param id       the song ID
     * @param musicDto the updated music data
     * @return the updated music
     */
    @PutMapping("/artist/{artistId}/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ARTIST')")
    public ResponseEntity<BaseResponse<MusicDto>> updateSongForArtist(
            @PathVariable int artistId, @PathVariable int id, @Valid @RequestBody MusicDto musicDto) {
        try {
            BaseResponse<MusicDto> updatedSong = musicService.updateSongForArtist(artistId, id, musicDto);
            return ResponseEntity.ok(updatedSong);
        } catch (ArtistException ex) {
            BaseResponse<MusicDto> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete a song for the artist. Accessible to ARTIST.
     *
     * @param artistId the artist's ID
     * @param id       the song ID
     * @return a success response
     */
    @DeleteMapping("/artist/{artistId}/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ARTIST')")
    public ResponseEntity<BaseResponse<Integer>> deleteSongForArtist(
            @PathVariable int artistId, @PathVariable int id) {
        try {
            BaseResponse<Integer> deletedId = musicService.deleteSongForArtist(artistId, id);
            return ResponseEntity.ok(deletedId);
        } catch (ArtistException ex) {
            BaseResponse<Integer> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
