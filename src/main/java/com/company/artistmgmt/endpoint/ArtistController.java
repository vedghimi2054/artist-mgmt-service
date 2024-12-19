
package com.company.artistmgmt.endpoint;

import com.company.artistmgmt.dto.ArtistDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.service.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/artists")
@Validated
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    /**
     * List all artists with pagination. Accessible to SUPER_ADMIN and ARTIST_MANAGER.
     *
     * @param pageNo   the page number (default: 0)
     * @param pageSize the number of records per page (default: 10)
     * @return paginated list of artists
     */
    @GetMapping
//    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ARTIST_MANAGER')")
    public ResponseEntity<BaseResponse<List<ArtistDto>>> listArtists(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            BaseResponse<List<ArtistDto>> artists = artistService.getAllArtists(pageNo, pageSize);
            return ResponseEntity.ok(artists);
        } catch (ArtistException ex) {
            BaseResponse<List<ArtistDto>> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Create a new artist. Accessible to ARTIST_MANAGER.
     *
     * @param artistDto the artist DTO
     * @return the created artist
     */
    @PostMapping
//    @PreAuthorize("hasRole('ARTIST_MANAGER')")
    public ResponseEntity<BaseResponse<ArtistDto>> createArtist(@Valid @RequestBody ArtistDto artistDto) {
        try {
            BaseResponse<ArtistDto> createdArtist = artistService.createArtist(artistDto);
            return ResponseEntity.ok(createdArtist);
        } catch (ArtistException ex) {
            BaseResponse<ArtistDto> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update an existing artist. Accessible to ARTIST_MANAGER.
     *
     * @param id        the artist ID
     * @param artistDto the updated artist data
     * @return the updated artist
     */
    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ARTIST_MANAGER')")
    public ResponseEntity<BaseResponse<ArtistDto>> updateArtist(@PathVariable int id, @Valid @RequestBody ArtistDto artistDto) {
        try {
            BaseResponse<ArtistDto> updatedArtist = artistService.updateArtist(id, artistDto);
            return ResponseEntity.ok(updatedArtist);
        } catch (ArtistException ex) {
            BaseResponse<ArtistDto> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete an artist by ID. Accessible to ARTIST_MANAGER.
     *
     * @param id the artist ID
     * @return a success response
     */
    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ARTIST_MANAGER')")
    public ResponseEntity<BaseResponse<Integer>> deleteArtist(@PathVariable int id) {
        try {
            BaseResponse<Integer> response = artistService.deleteArtist(id);
            return ResponseEntity.ok(response);
        } catch (ArtistException ex) {
            BaseResponse<Integer> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get an artist by ID. Accessible to SUPER_ADMIN and ARTIST_MANAGER.
     *
     * @param id the artist ID
     * @return the artist details
     */
    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ARTIST_MANAGER')")
    public ResponseEntity<BaseResponse<ArtistDto>> getArtistById(@PathVariable int id) {
        try {
            BaseResponse<ArtistDto> artist = artistService.getArtistById(id);
            return ResponseEntity.ok(artist);
        } catch (ArtistException ex) {
            BaseResponse<ArtistDto> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),  // Error status code
                    ex.getMessage()                   // Error message
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        }
    }
}

