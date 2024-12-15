package com.company.artistmgmt.repository;

import com.company.artistmgmt.config.DBConnection;
import com.company.artistmgmt.config.DBResultSet;
import com.company.artistmgmt.exception.RepoException;
import com.company.artistmgmt.exception.RepoRuntimeException;
import com.company.artistmgmt.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AccountRepoImpl implements AccountRepo {
    private static final Logger logger = LoggerFactory.getLogger(AccountRepoImpl.class);
    private static final String QUERY = "Query:{}";
    private final DBConnection dataSource;

    private final DBResultSet resultSet;

    public AccountRepoImpl(@Qualifier("datasource") DBConnection dataSource, @Qualifier("resultSet") DBResultSet resultSet) {
        this.dataSource = dataSource;
        this.resultSet = resultSet;
    }

    @Override
    public List<User> getAllUsers() throws RepoException {
        logger.debug("Getting all users:");
        var query = "SELECT * FROM anydone.account where account_id = '41e332cbcd3f442386dae88f07827bbb' ORDER BY created_at DESC";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            logger.debug(QUERY, query);
            return resultSet.getResults(statement.executeQuery(), this::extractUserInfo);
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

    private User extractUserInfo(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId(resultSet.getString("account_id"));
            user.setEmail(resultSet.getString("email"));
            return user;
        } catch (Exception ex) {
            throw new RepoRuntimeException("Error while extract user info", ex);
        }

    }
}
