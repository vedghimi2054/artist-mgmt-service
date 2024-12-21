package com.company.artistmgmt.repository;

import com.company.artistmgmt.config.DBResultSet;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.exception.ArtistRuntimeException;
import com.company.artistmgmt.exception.FailedException;
import com.company.artistmgmt.model.Artist;
import com.company.artistmgmt.model.general.Gender;
import com.company.artistmgmt.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import static com.company.artistmgmt.repository.QueryConst.*;

@Repository
public class ArtistRepoImpl implements ArtistRepo {
    private static final Logger logger = LoggerFactory.getLogger(ArtistRepoImpl.class);
    private final DataSource dataSource;

    private final DBResultSet resultSet;

    public ArtistRepoImpl(DataSource dataSource, @Qualifier("resultSet") DBResultSet resultSet) {
        this.dataSource = dataSource;
        this.resultSet = resultSet;
    }

    @Override
    public Artist createArtist(Artist artistEntity) throws ArtistException {
        logger.debug("Creating Artist:{}", artistEntity);
        var query = "INSERT INTO `artist` (name, dob, gender, address, first_release_year, no_of_albums_released) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            logger.debug(QueryConst.QUERY, query);

            var i = 0;
            statement.setString(++i, artistEntity.getName());
            statement.setTimestamp(++i, artistEntity.getDob() != null ?
                    Timestamp.valueOf(artistEntity.getDob()) : null);
            statement.setInt(++i, artistEntity.getGender() != null ? artistEntity.getGender().getValue() : 0);
            statement.setString(++i, artistEntity.getAddress());
            statement.setInt(++i, artistEntity.getFirstReleaseYear());
            statement.setInt(++i, artistEntity.getNoOfAlbumsReleased());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new FailedException("Failed to create artist.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    artistEntity.setId(generatedId);
                } else {
                    throw new FailedException("Failed to retrieve generated ID for artist.");
                }
            }
        } catch (SQLException e) {
            throw new ArtistException("Error while create artist." + e.getMessage(), e);
        }
        return artistEntity;
    }

    @Override
    public boolean checkArtistExistsById(int id) throws ArtistException {
        logger.debug("Checking Artist exists by id:{}", id);
        var query = SELECT + "COUNT(id)" + FROM + ARTIST_TABLE + WHERE + " id = ?";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, id);
            return resultSet.count(statement.executeQuery()) <= 0;
        } catch (SQLException e) {
            throw new ArtistException("Error while checking artist exists by id."+ e.getMessage(), e);
        }
    }

    @Override
    public boolean updateArtist(int id, Artist artistEntity) {
        logger.debug("Updating Artist with ID: {}", id);

        String query = "UPDATE `artist` SET name = ?, dob = ?, gender = ?, address = ?, first_release_year = ?, " +
                "no_of_albums_released = ? WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {

            logger.debug(QueryConst.QUERY, query);

            int i = 0;
            statement.setString(++i, artistEntity.getName());
            statement.setTimestamp(++i, artistEntity.getDob() != null ?
                    Timestamp.valueOf(artistEntity.getDob()) : null);
            statement.setInt(++i, artistEntity.getGender() != null ? artistEntity.getGender().getValue() : 0);
            statement.setString(++i, artistEntity.getAddress());
            statement.setInt(++i, artistEntity.getFirstReleaseYear());
            statement.setInt(++i, artistEntity.getNoOfAlbumsReleased());
            statement.setInt(++i, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 1) {
                logger.debug("Artist updated successfully.");
                return true;
            } else {
                logger.warn("No artist found with ID: {}", id);
                throw new ArtistRuntimeException("Artist not found or not updated.");
            }

        } catch (SQLException e) {
            logger.error("Error while updating artist with ID: {}", id, e);
            throw new ArtistRuntimeException("Error while updating artist."+ e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteArtist(int id) {
        logger.info("Deleting the artist by . Id: {}", id);
        var query = "DELETE FROM artist where id = ?";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            logger.error("Error Deleting artist by id:{}", id, ex);
            return false;
        }
    }

    @Override
    public List<Artist> getAllArtists(int validatePageSize, int offset) throws ArtistException {
        logger.debug("Getting all users with validatePageSize{}: and offset:{}", validatePageSize, offset);
        var query = "SELECT * FROM artist ORDER BY created_at DESC LIMIT ? OFFSET ?";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, validatePageSize);
            statement.setInt(2, offset);
            return resultSet.getResults(statement.executeQuery(), this::extractArtistInfo);
        } catch (SQLException e) {
            throw new ArtistException(e);
        }
    }

    @Override
    public Artist getArtistById(int id) throws ArtistException {
        logger.debug("Getting artist by id:{}", id);
        var query = SELECT + ARTIST_FETCH_COLUMN_QUERY + FROM + ARTIST_TABLE + WHERE + " id = ?";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QUERY, query);
            statement.setInt(1, id);
            return resultSet.getResult(statement.executeQuery(), this::extractArtistInfo);
        } catch (SQLException e) {
            throw new ArtistException(e);
        }
    }

    @Override
    public long getArtistCount() throws ArtistException {
        logger.debug("Count Artist:");
        var query = SELECT + "COUNT(id)" + FROM + ARTIST_TABLE;
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            return resultSet.count(statement.executeQuery());
        } catch (SQLException e) {
            throw new ArtistException("Error while count artist."+ e.getMessage(), e);
        }
    }

    private Artist extractArtistInfo(ResultSet resultSet) {
        try {
            Artist artist = new Artist();
            artist.setId(resultSet.getInt("id"));
            artist.setName(StringUtils.getNotNullString(resultSet.getString("name")));
            Timestamp dobTimestamp = resultSet.getTimestamp("dob");
            if (dobTimestamp != null) {
                artist.setDob(dobTimestamp.toLocalDateTime());
            }
            int genderValue = resultSet.getInt("gender");
            if (!resultSet.wasNull()) {
                artist.setGender(Gender.fromValue(genderValue));
            }

            artist.setAddress(StringUtils.getNotNullString(resultSet.getString("address")));
            artist.setFirstReleaseYear(resultSet.getInt("first_release_year"));
            artist.setNoOfAlbumsReleased(resultSet.getInt("no_of_albums_released"));
            Timestamp createdAt = resultSet.getTimestamp("created_at");
            if (createdAt != null) {
                artist.setCreatedAt(createdAt.toLocalDateTime());
            }
            Timestamp updatedAt = resultSet.getTimestamp("updated_at");
            if (updatedAt != null) {
                artist.setUpdatedAt(updatedAt.toLocalDateTime());
            }

            return artist;
        } catch (Exception ex) {
            throw new ArtistRuntimeException("Error while extracting artist info."+ ex.getMessage(), ex);
        }
    }

}
