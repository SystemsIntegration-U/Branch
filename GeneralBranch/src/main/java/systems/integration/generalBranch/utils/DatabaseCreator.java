package systems.integration.generalBranch.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreator {

    private static final String URL = "jdbc:postgresql://postgres:5432/";
    private static final String USER = System.getenv("POSTGRES_USER");
    private static final String PASSWORD = System.getenv("POSTGRES_PASSWORD");

    public static void createDatabase(String dbName) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE DATABASE " + dbName;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            if (e.getSQLState().equals("42P04")) {
                System.out.println("The database " + dbName + " already exists");
            } else {
                e.printStackTrace();
            }
        }
    }
}