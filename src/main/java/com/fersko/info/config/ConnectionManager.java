package com.fersko.info.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String URL_KEY = "db.url";
    private static final String DRIVER_KEY = "db.driver";
    private static final HikariDataSource hikariDataSource;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(PropertiesUtils.get(URL_KEY));
        hikariConfig.setUsername(PropertiesUtils.get(USERNAME_KEY));
        hikariConfig.setPassword(PropertiesUtils.get(PASSWORD_KEY));
        hikariConfig.setDriverClassName(PropertiesUtils.get(DRIVER_KEY));

        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

}
