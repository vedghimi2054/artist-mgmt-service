package com.company.artistmgmt.repository;

import com.company.artistmgmt.config.DBResultSet;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.exception.ArtistRuntimeException;
import com.company.artistmgmt.exception.FailedException;
import com.company.artistmgmt.model.Music;
import com.company.artistmgmt.model.general.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static com.company.artistmgmt.repository.QueryConst.*;

@Repository
public class MusicRepoImpl implements MusicRepo {
    private static final Logger logger = LoggerFactory.getLogger(MusicRepoImpl.class);

    private final DataSource dataSource;
    private final DBResultSet resultSet;

    public MusicRepoImpl(DataSource dataSource, @Qualifier("resultSet") DBResultSet resultSet) {
        this.dataSource = dataSource;
        this.resultSet = resultSet;
    }

    @Override
    public Music createSongForArtist(int artistId, Music musicEntity) throws ArtistException {
        logger.debug("Creating songs for artist id:{} with: Payload:{}", artistId, musicEntity);
        String query = "INSERT INTO `music` (title, artist_id, album_name, genre) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setString(1, musicEntity.getTitle());
            statement.setInt(2, artistId);
            statement.setString(3, musicEntity.getAlbumName());
            statement.setInt(4, musicEntity.getGenre().getValue());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new FailedException("Failed to create songs for artist.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    musicEntity.setId(generatedId);
                } else {
                    throw new FailedException("Failed to retrieve generated ID for artist.");
                }
            }
        } catch (SQLException e) {
            throw new ArtistException("Error while creating song for artist."+ e.getMessage(), e);
        }
        return musicEntity;
    }


    @Override
    public boolean updateSongForArtist(int artistId, int id, Music musicEntity) throws ArtistException {
        logger.debug("Updating songs for artist id:{} and musicId:{} with: Payload:{}", artistId, id, musicEntity);
        String query = "UPDATE `music` SET title = ?, album_name = ?, genre = ? WHERE id = ? AND artist_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setString(1, musicEntity.getTitle());
            statement.setString(2, musicEntity.getAlbumName());
            statement.setInt(3, musicEntity.getGenre().getValue());
            statement.setInt(4, id);
            statement.setInt(5, artistId);

            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new ArtistException("Error while updating song for artist."+ e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteSongForArtist(int artistId, int id) throws ArtistException {
        logger.debug("Deleting songs for artist id:{} and musicId:{}", artistId, id);
        String query = "DELETE FROM `music` WHERE id = ? AND artist_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, id);
            statement.setInt(2, artistId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new ArtistException("Error while deleting song for artist.", e);
        }
    }

    @Override
    public List<Music> getSongsByArtist(int artistId, int validatePageSize, int offset) throws ArtistException {
        logger.debug("Getting songs by artist with: Payload:{}", artistId);
        String query = SELECT + MUSIC_FETCH_COLUMN_QUERY + FROM + MUSIC_TABLE +
                " INNER JOIN artist.artist a on m.artist_id = a.id " +
                WHERE + "m.artist_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, artistId);
            statement.setInt(2, validatePageSize);
            statement.setInt(3, offset);
            return resultSet.getResults(statement.executeQuery(), this::extractMusicInfo);
        } catch (SQLException e) {
            throw new ArtistException("Error while fetching songs for artist."+ e.getMessage(), e);
        }
    }

    @Override
    public Music getSongById(int id, int artistId) throws ArtistException {
        logger.debug("Getting song by id:{} and artistId:{}", id, artistId);
        String query = "SELECT * FROM music WHERE id = ? AND artist_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, id);
            statement.setInt(2, artistId);
            return resultSet.getResult(statement.executeQuery(), this::extractMusicInfo);
        } catch (SQLException e) {
            throw new ArtistException("Error while fetching song by ID."+ e.getMessage(), e);
        }
    }

    @Override
    public long countSongsByArtist(int artistId) throws ArtistException {
        logger.debug("Count Songs by artistId:{}:", artistId);
        var query = SELECT + "COUNT(id)" + FROM + MUSIC_TABLE + WHERE + "artist_id =?";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, artistId);
            return resultSet.count(statement.executeQuery());
        } catch (SQLException e) {
            throw new ArtistException("Error while count music.", e);
        }
    }

    @Override
    public boolean checkMusicExistsById(int id, int artistId) throws ArtistException {
        logger.debug("Checking music exists with id:{} for artistId:{}", id, artistId);
        String query = "SELECT COUNT(id) FROM music WHERE id = ? AND artist_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, id);
            statement.setInt(2, artistId);
            return resultSet.count(statement.executeQuery()) <= 0;
        } catch (SQLException e) {
            throw new ArtistException("Error while fetching song by ID."+ e.getMessage(), e);
        }
    }

    private Music extractMusicInfo(ResultSet resultSet) {
        try {
            Music music = new Music();
            music.setId(resultSet.getInt("m.id"));
            music.setTitle(resultSet.getString("m.title"));
            music.setArtistId(resultSet.getInt("m.artist_id"));
            music.setAlbumName(resultSet.getString("m.album_name"));
            int genre = resultSet.getInt("m.genre");
            music.setGenre(Genre.fromValue(genre));
            Timestamp createdAt = resultSet.getTimestamp("m.created_at");
            if (createdAt != null) {
                music.setCreatedAt(createdAt.toLocalDateTime());
            }
            Timestamp updatedAt = resultSet.getTimestamp("m.updated_at");
            if (updatedAt != null) {
                music.setUpdatedAt(updatedAt.toLocalDateTime());
            }

            return music;
        } catch (Exception ex) {
            throw new ArtistRuntimeException("Error while extracting music info."+ ex.getMessage(), ex);
        }
    }
}

