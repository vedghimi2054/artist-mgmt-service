package com.company.artistmgmt.service;

import com.company.artistmgmt.dto.ArtistDto;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.exception.FailedException;
import com.company.artistmgmt.exception.ResourceNotFoundException;
import com.company.artistmgmt.exception.ValidationException;
import com.company.artistmgmt.mapper.ArtistMapper;
import com.company.artistmgmt.model.Artist;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.model.general.Gender;
import com.company.artistmgmt.repository.ArtistRepo;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.artistmgmt.mapper.ArtistMapper.toArtistDto;
import static com.company.artistmgmt.mapper.ArtistMapper.toArtistEntity;
import static com.company.artistmgmt.util.PaginationUtils.validateAndSetDefaults;

@Service
public class ArtistServiceImpl implements ArtistService {

    private static final Logger logger = LoggerFactory.getLogger(ArtistServiceImpl.class);

    private final ArtistRepo artistRepo;

    public ArtistServiceImpl(ArtistRepo artistRepo) {
        this.artistRepo = artistRepo;
    }

    @Override
    public BaseResponse<ArtistDto> createArtist(ArtistDto artistDto) throws ArtistException {
        logger.debug("Creating artist with: Payload:{}", artistDto);
        validateArtistDto(artistDto);
        Artist artistEntity = toArtistEntity(artistDto);
        Artist artist = artistRepo.createArtist(artistEntity);
        logger.debug("After insert:{}", artist);
        Artist artistById = artistRepo.getArtistById(artist.getId());
        ArtistDto dto = toArtistDto(artistById);
        return new BaseResponse<>(true, dto);
    }

    @Override
    public BaseResponse<ArtistDto> updateArtist(int id, ArtistDto artistDto) throws ArtistException {
        logger.debug("Updating artist with id {} : Payload:{}", id, artistDto);
        validateId(id);

        if (!artistRepo.checkArtistExistsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }
        validateArtistDto(artistDto);
        Artist artistEntity = toArtistEntity(artistDto);
        if (!artistRepo.updateArtist(id, artistEntity)) {
            throw new FailedException("Failed to update artist.");
        }
        Artist artistById = artistRepo.getArtistById(id);
        ArtistDto updatedArtistDto = toArtistDto(artistById);
        return new BaseResponse<>(true, updatedArtistDto);

    }

    @Override
    public BaseResponse<List<ArtistDto>> getAllArtists(int pageNo, int pageSize) throws ArtistException {
        logger.debug("Getting all artist with pageNo:{} and pageSize:{}", pageNo, pageSize);
        // Validate and set default pagination values
        if (pageNo < 0 || pageSize <= 0) {
            throw new ValidationException("pageNo must be >= 0 and pageSize must be > 0");
        }
        // Calculate offset
        int offset = pageNo * pageSize;
        List<Artist> allArtists = artistRepo.getAllArtists(pageSize, offset);
        long totalCount = artistRepo.getArtistCount(); // Get total count
        List<ArtistDto> artistDtos = allArtists.stream()
                .map(ArtistMapper::toArtistDto)
                .collect(Collectors.toList());

        // Add pagination details to response
        BaseResponse<List<ArtistDto>> response = new BaseResponse<>();
        response.setSuccess(true);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage("Artist Fetch successfully");
        response.setStatusCode(HttpStatus.OK.value());
        response.setDataResponse(artistDtos);
        response.addMeta("totalCount", totalCount);
        response.addMeta("totalPages", (int) Math.ceil((double) totalCount / pageSize));
        response.addMeta("currentPage", pageNo);
        response.addMeta("pageSize", pageSize);
        return response;
    }

    @Override
    public BaseResponse<Integer> deleteArtist(int artistId) throws ArtistException {
        logger.debug("Deleting artist with id {} ", artistId);
        validateId(artistId);

        if (!artistRepo.checkArtistExistsById(artistId)) {
            throw new ResourceNotFoundException("Artist with ID " + artistId + " not found");
        }
        if (!artistRepo.deleteArtist(artistId)) {
            throw new FailedException("Failed to delete artist.");
        }
        return new BaseResponse<>(true, artistId);
    }

    @Override
    public BaseResponse<ArtistDto> getArtistById(int id) throws ArtistException {
        logger.debug("Getting artist by id:{}", id);
        validateId(id);
        if (!artistRepo.checkArtistExistsById(id)) {
            throw new ResourceNotFoundException("Artist with ID " + id + " not found");
        }
        Artist artistById = artistRepo.getArtistById(id);
        ArtistDto artistDto = toArtistDto(artistById);
        return new BaseResponse<>(true, artistDto);
    }

    @Override
    public void exportArtistsToCsv(PrintWriter writer) throws ArtistException {
        logger.debug("Exporting artists to csv");
        List<Artist> artists = artistRepo.getAllArtists(0, 10);

        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            // Write header
            String[] header = {"ID", "Name", "DOB", "Gender", "Address", "First Release Year", "No of Albums Released"};
            csvWriter.writeNext(header);

            // Write data
            for (Artist artist : artists) {
                String[] data = {
                        String.valueOf(artist.getId()),
                        artist.getName(),
                        String.valueOf(artist.getDob()),
                        String.valueOf(artist.getGender().getValue()),
                        artist.getAddress(),
                        String.valueOf(artist.getFirstReleaseYear()),
                        String.valueOf(artist.getNoOfAlbumsReleased())
                };
                csvWriter.writeNext(data);
            }
        } catch (IOException e) {
            throw new ArtistException("Failed to export csv.", e);
        }
    }

    /**
     * Validates the artist ID.
     */
    private void validateId(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid artist ID");
        }
    }

    /**
     * Validates the ArtistDto object.
     */
    private void validateArtistDto(ArtistDto artistDto) {
        if (artistDto.getName() == null || artistDto.getName().trim().isEmpty()) {
            throw new ValidationException("Name cannot be empty.");
        }

        // Check if 'dob' is not null
        if (artistDto.getDob() == null) {
            throw new ValidationException("Date of birth cannot be null.");
        }

        // Check if 'address' is not null or empty
        if (artistDto.getAddress() == null || artistDto.getAddress().trim().isEmpty()) {
            throw new ValidationException("Address cannot be empty.");
        }

        // Check if 'firstReleaseYear' is not negative or zero
        if (artistDto.getFirstReleaseYear() <= 0) {
            throw new ValidationException("First release year must be greater than zero.");
        }

        // Check if 'noOfAlbumsReleased' is not negative
        if (artistDto.getNoOfAlbumsReleased() < 0) {
            throw new ValidationException("Number of albums released cannot be negative.");
        }
        if (artistDto.getGender() != null && !Gender.isValid(artistDto.getGender().name())) {
            throw new ValidationException("Invalid gender value");
        }
    }
}