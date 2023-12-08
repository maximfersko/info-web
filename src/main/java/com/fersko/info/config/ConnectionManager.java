package com.fersko.info.config;

import com.fersko.info.exceptions.ConnectionBDException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String URL_KEY = "db.url";
    private static final String DRIVER_KEY = "db.driver";

    private ConnectionManager() {

    }

    static {
        try {
            Class.forName(PropertiesUtils.get(DRIVER_KEY));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnections() throws SQLException {
        return DriverManager.getConnection(
                PropertiesUtils.get(URL_KEY),
                PropertiesUtils.get(USERNAME_KEY),
                PropertiesUtils.get(PASSWORD_KEY)
        );

    }

}
