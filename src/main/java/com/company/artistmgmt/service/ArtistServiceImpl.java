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
import com.company.artistmgmt.util.ImportArtistUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
        int offset = (pageSize * pageNo) - pageSize;
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
        logger.debug("Getting all artists.");
        var artists = artistRepo.getAllArtists(0, 10);
        try (Writer writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "ID", "Name", "DOB", "Gender", "Address", "First Release Year", "No of Albums Released"))) {

            // Write artist data rows
            for (Artist artist : artists) {
                csvPrinter.printRecord(
                        artist.getId(),
                        artist.getName(),
                        artist.getDob(),
                        artist.getGender().toString(),
                        artist.getAddress(),
                        artist.getFirstReleaseYear(),
                        artist.getNoOfAlbumsReleased()
                );
            }

            // Ensure data is written to the file
            csvPrinter.flush();

        } catch (IOException e) {
            throw new ArtistException("Failed to export CSV file", e);
        }
    }

    @Override
    public BaseResponse<ArtistDto> importArtistsFromCsv(MultipartFile file) throws ArtistException {
        logger.debug("Importing artists from CSV.");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                     .withQuote('"')  // Handle quoted values properly
                     .withEscape('\\')
                     .withDelimiter(',')
                     .withIgnoreEmptyLines())) {

            // Loop through the records in the CSV file
            for (CSVRecord record : csvParser) {
                // Skip if header or invalid data is encountered (it's already handled by withFirstRecordAsHeader)
                if (record.size() < 7) {
                    continue; // Skip the record if it doesn't have enough columns
                }

                // Create and save artist entity from CSV columns
                Artist artist = extractArtistRecord(record);

                // Save the artist to the database
                Artist createdArtist = artistRepo.createArtist(artist);
                Artist artistById = artistRepo.getArtistById(createdArtist.getId());
                ArtistDto dto = toArtistDto(artistById);
                return new BaseResponse<>(true, dto);
            }

        } catch (IOException e) {
            throw new ArtistException("Failed to process the CSV file." + e.getMessage(), e);
        } catch (Exception e) {
            throw new ArtistException("Invalid data format in the CSV file." + e.getMessage(), e);
        }

        return null;

    }

    private static Artist extractArtistRecord(CSVRecord record) {

        Artist artist = new Artist();
        artist.setId(ImportArtistUtils.parseNumber(record.get(0)));
        artist.setName(ImportArtistUtils.getValidteString(record.get(1)));
        artist.setDob(ImportArtistUtils.parseDOB(record.get(2)));
        artist.setGender(ImportArtistUtils.parseGender(record.get(3)));
        artist.setAddress(ImportArtistUtils.getValidteString(record.get(4)));
        artist.setFirstReleaseYear(ImportArtistUtils.parseNumber(record.get(5)));
        artist.setNoOfAlbumsReleased(ImportArtistUtils.parseNumber(record.get(6)));
        return artist;
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