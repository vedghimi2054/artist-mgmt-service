package com.company.artistmgmt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component("datasource")
public class DBConnection {
    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(dbDriver);
            String hostAndPort = extractHostAndPort(dbUrl);
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            logger.info("Database connected successfully with username '{}' at '{}'", dbUsername, hostAndPort);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error while getting database connection: {}", e.getMessage());
            throw new SQLException("Error while getting database connection", e);
        }
    }

    private String extractHostAndPort(String dbUrl) {
        try {
            String[] parts = dbUrl.split("//")[1].split("/");
            return parts[0]; // host:port
        } catch (Exception e) {
            logger.warn("Failed to extract host and port from URL '{}'", dbUrl);
            return "unknown host and port";
        }
    }
}
