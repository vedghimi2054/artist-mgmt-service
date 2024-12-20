package com.company.artistmgmt.repository;

import com.company.artistmgmt.config.DBResultSet;
import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.exception.ArtistRuntimeException;
import com.company.artistmgmt.exception.FailedException;
import com.company.artistmgmt.model.User;
import com.company.artistmgmt.model.general.Gender;
import com.company.artistmgmt.model.general.Role;
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
public class UserRepoImpl implements UserRepo {
    private static final Logger logger = LoggerFactory.getLogger(UserRepoImpl.class);
    private final DataSource dataSource;

    private final DBResultSet resultSet;

    public UserRepoImpl(DataSource dataSource, @Qualifier("resultSet") DBResultSet resultSet) {
        this.dataSource = dataSource;
        this.resultSet = resultSet;
    }

    @Override
    public List<User> getAllUsers(int validatedPageSize, int offset) throws ArtistException {
        logger.debug("Getting all users:{}", validatedPageSize);
        var query = "SELECT * FROM user ORDER BY created_at DESC LIMIT ? OFFSET ?";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, validatedPageSize);
            statement.setInt(2, offset);
            return resultSet.getResults(statement.executeQuery(), this::extractUserInfo);
        } catch (SQLException e) {
            throw new ArtistException(e);
        }
    }

    @Override
    public User createUser(User userEntity) throws ArtistException {
        logger.debug("Creating User");
        var query = "INSERT INTO `user` (first_name, last_name, email, password, phone, dob, gender, address, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            logger.debug(QueryConst.QUERY, query);

            var i = 0;
            statement.setString(++i, userEntity.getFirstName());
            statement.setString(++i, userEntity.getLastName());
            statement.setString(++i, userEntity.getEmail());
            statement.setString(++i, userEntity.getPassword());
            statement.setString(++i, userEntity.getPhone());
            statement.setTimestamp(++i, userEntity.getDob() != null ?
                    Timestamp.valueOf(userEntity.getDob()) : null);
            statement.setInt(++i, userEntity.getGender().getValue());
            statement.setString(++i, userEntity.getAddress());
            statement.setInt(++i, userEntity.getRole().getValue());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new FailedException("Failed to create user.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    userEntity.setId(generatedId);
                } else {
                    throw new FailedException("Failed to retrieve generated ID for user.");
                }
            }
        } catch (SQLException e) {
            throw new ArtistException("Error while creating user." + e.getMessage(), e);
        }
        return userEntity;
    }

    @Override
    public boolean existsById(int id) throws ArtistException {
        logger.debug("Getting User by id:{}", id);
        var query = SELECT + "COUNT(id)" + FROM + USER_TABLE + WHERE + " id = ?";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, id);
            return resultSet.count(statement.executeQuery()) <= 0;
        } catch (SQLException e) {
            throw new ArtistException(e);
        }
    }

    @Override
    public boolean updateUser(int id, User useEntity) {
        logger.debug("Updating User with ID: {}", id);

        String query = "UPDATE `user` SET first_name = ?, last_name = ?, email = ?, phone = ?, " +
                "dob = ?, gender = ?, address = ?, role = ? WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {

            logger.debug("Executing Query: {}", query);

            int i = 0;
            statement.setString(++i, useEntity.getFirstName());
            statement.setString(++i, useEntity.getLastName());
            statement.setString(++i, useEntity.getEmail());
            statement.setString(++i, useEntity.getPhone());
            statement.setTimestamp(++i, useEntity.getDob() != null ?
                    Timestamp.valueOf(useEntity.getDob()) : null);
            statement.setInt(++i, useEntity.getGender() != null ?
                    useEntity.getGender().getValue() : 0);
            statement.setString(++i, useEntity.getAddress());
            statement.setInt(++i, useEntity.getRole() != null ?
                    useEntity.getRole().getValue() : 0);
            statement.setInt(++i, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 1) {
                logger.debug("User updated successfully.");
                return true;
            } else {
                logger.warn("No user found with ID: {}", id);
                throw new ArtistRuntimeException("User not found or not updated.");
            }

        } catch (SQLException e) {
            logger.error("Error while updating user with ID: {}", id, e);
            throw new ArtistRuntimeException("Error while updating user.", e);
        }
    }

    @Override
    public boolean deleteUser(int id) {
        logger.info("Deleting the user by . Id: {}", id);
        var query = "DELETE FROM user.user where id = ?";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            logger.error("Error Deleting user by id:{}", id, ex);
            return false;
        }
    }

    @Override
    public User getUserById(int id) throws ArtistException {
        logger.debug("Getting User detail by id:{}", id);
        var query = SELECT + USER_FETCH_COLUMN_QUERY + FROM + USER_TABLE + WHERE + " id = ?";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setInt(1, id);
            return resultSet.getResult(statement.executeQuery(), this::extractUserInfo);
        } catch (SQLException e) {
            throw new ArtistException(e);
        }
    }

    @Override
    public long countTotalUsers() throws ArtistException {
        logger.debug("Count Total users");
        var query = SELECT + "COUNT(id)" + FROM + USER_TABLE;
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            return resultSet.count(statement.executeQuery());
        } catch (SQLException e) {
            throw new ArtistException("Error while count user.", e);
        }
    }

    @Override
    public User findUserByEmail(String email) throws ArtistException {
        logger.debug("Find user by email:{}", email);
        var query = SELECT + USER_FETCH_COLUMN_QUERY + FROM + USER_TABLE + WHERE + "a.email = ?";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QueryConst.QUERY, query);
            statement.setString(1, email);
            logger.debug("Execute query:{}", statement.toString());
            return resultSet.getResult(statement.executeQuery(), this::extractUserInfo);
        } catch (SQLException e) {
            throw new ArtistException("Error while find user by email.", e);
        }
    }

    private User extractUserInfo(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setFirstName(StringUtils.getNotNullString(resultSet.getString("first_name")));
            user.setLastName(StringUtils.getNotNullString(resultSet.getString("last_name")));
            user.setEmail(StringUtils.getNotNullString(resultSet.getString("email")));
            user.setPhone(StringUtils.getNotNullString(resultSet.getString("phone")));
            user.setPassword(StringUtils.getNotNullString(resultSet.getString("password")));
            // Map timestamp to LocalDateTime for dob
            Timestamp dobTimestamp = resultSet.getTimestamp("dob");
            if (dobTimestamp != null) {
                user.setDob(dobTimestamp.toLocalDateTime());
            }
            // Map gender if present (assuming Gender is an enum)
            int genderValue = resultSet.getInt("gender");
            if (!resultSet.wasNull()) {
                user.setGender(Gender.fromValue(genderValue));
            }
            user.setAddress(StringUtils.getNotNullString(resultSet.getString("address")));
            // Map role (assuming Role is an enum)
            int roleValue = resultSet.getInt("role");
            if (!resultSet.wasNull()) {
                user.setRole(Role.fromValue(roleValue));
            }
            // Map timestamps to LocalDateTime
            Timestamp createdAt = resultSet.getTimestamp("created_at");
            if (createdAt != null) {
                user.setCreatedAt(createdAt.toLocalDateTime());
            }
            Timestamp updatedAt = resultSet.getTimestamp("updated_at");
            if (updatedAt != null) {
                user.setUpdatedAt(updatedAt.toLocalDateTime());
            }
            return user;
        } catch (Exception ex) {
            throw new ArtistRuntimeException("Error while extract user info.", ex);
        }
    }
}
