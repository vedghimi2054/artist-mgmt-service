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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.artistmgmt.mapper.ArtistMapper.toArtistDto;
import static com.company.artistmgmt.mapper.ArtistMapper.toArtistEntity;
import static com.company.artistmgmt.repository.QueryConst.*;
import static com.company.artistmgmt.util.MetaUtils.extractedMeta;

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
        Artist artistById = artistRepo.getArtistById(artist.getId());
        ArtistDto dto = toArtistDto(artistById);
        return new BaseResponse<>(true, dto);
    }

    @Override
    public BaseResponse<ArtistDto> updateArtist(int id, ArtistDto artistDto) throws ArtistException {
        logger.debug("Updating artist with id {} : Payload:{}", id, artistDto);
        validateId(id);

        if (artistRepo.checkArtistExistsById(id)) {
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
        response.setMessage(ARTIST_SUCCESS_MSG);
        response.setDataResponse(artistDtos);
        extractedMeta(pageNo, pageSize, response, totalCount);
        return response;
    }


    @Override
    public BaseResponse<Integer> deleteArtist(int artistId) throws ArtistException {
        logger.debug("Deleting artist with id {} ", artistId);
        validateId(artistId);

        if (artistRepo.checkArtistExistsById(artistId)) {
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
        if (artistRepo.checkArtistExistsById(id)) {
            throw new ResourceNotFoundException("Artist with ID " + id + " not found");
        }
        Artist artistById = artistRepo.getArtistById(id);
        ArtistDto artistDto = toArtistDto(artistById);
        return new BaseResponse<>(true, artistDto);
    }

    @Override
    public void exportArtistsToCsv(String filePath) throws ArtistException {
        List<Artist> artists = artistRepo.getAllArtists(0, 10);

        try (Writer writer = new FileWriter(filePath);
             CSVWriter csvWriter = new CSVWriter(writer)) {
            // Write CSV header
            String[] header = {"ID", "Name", "DOB", "Gender", "Address", "First Release Year", "No of Albums Released"};
            csvWriter.writeNext(header);

            // Write artist data
            for (Artist artist : artists) {
                String[] data = {
                        String.valueOf(artist.getId()),
                        artist.getName(),
                        String.valueOf(artist.getDob()),
                        artist.getGender().toString(),
                        artist.getAddress(),
                        String.valueOf(artist.getFirstReleaseYear()),
                        String.valueOf(artist.getNoOfAlbumsReleased())
                };
                csvWriter.writeNext(data);
            }
        } catch (IOException e) {
            throw new ArtistException("Failed to export CSV file", e);
        }
    }

    @Override
    public BaseResponse<ArtistDto> importArtistsFromCsv(MultipartFile file) throws ArtistException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true; // To skip the header

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                // Split CSV line into columns
                String[] columns = line.split(",");

                // Create and save artist entity
                Artist artist = new Artist();
                artist.setId(Integer.parseInt(columns[0].replace("\"", "").trim()));
                artist.setName(columns[1].replace("\"", "").trim());
                artist.setDob(LocalDateTime.parse(columns[2].replace("\"", "").trim()));
                artist.setGender(Gender.valueOf(columns[3].replace("\"", "").trim().toUpperCase()));
                artist.setAddress(columns[4].replace("\"", "").trim());
                artist.setFirstReleaseYear(Integer.parseInt(columns[5].replace("\"", "").trim()));
                artist.setNoOfAlbumsReleased(Integer.parseInt(columns[6].replace("\"", "").trim()));

                Artist createdArtist = artistRepo.createArtist(artist);
                Artist artistById = artistRepo.getArtistById(createdArtist.getId());
                ArtistDto dto = toArtistDto(artistById);
                return new BaseResponse<>(true, dto);
            }
        } catch (IOException e) {
            throw new ArtistException("Failed to process the CSV file.", e);
        } catch (Exception e) {
            throw new ArtistException("Invalid data format in the CSV file.", e);
        }
        return null;
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
        if (artistDto.getGender() != null && Gender.isValid(artistDto.getGender().name())) {
            throw new ValidationException("Invalid gender value");
        }
    }
}