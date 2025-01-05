package com.company.artistmgmt.config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public enum DataSourceSingleton {
    INSTANCE;

    private final HikariDataSource dataSource;

    DataSourceSingleton() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/artist?useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("root");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);
        config.setMaxLifetime(1800000);

        dataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
