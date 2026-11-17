package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(System.getenv("DB_URL"),System.getenv("DB_USERNAME"), System.getenv("DB_PASSWORD"));
    }
}
