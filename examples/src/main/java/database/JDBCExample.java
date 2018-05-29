package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExample {
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_PORT_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_NAME = "test_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "password";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRIVER);
        JDBCExample main = new JDBCExample();
        main.databaseCreateIfNeeded(DB_NAME);
        //main.databaseDrop(DB_NAME);
    }

    private void databaseCreateIfNeeded(String name) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_PORT_URL, DB_USER, DB_PASS)) {
            if (!dbExists(conn, name)) {
                System.out.println("Database not found, creating");
                this.dbCreate(conn, name);
            } else {
                System.out.println("Database found");
            }
        }
    }

    private void databaseDrop(String name) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_PORT_URL, DB_USER, DB_PASS)) {
            this.dbDrop(conn, name);
        }
    }

    private boolean dbExists(Connection conn, String name) throws SQLException {
        boolean result = false;
        try (Statement stmt = conn.createStatement()) {


        }
        return result;
    }

    private void dbCreate(Connection conn, String name) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String query = String.format("CREATE DATABASE %s", name);
            stmt.executeUpdate(query);
        }
    }

    private void dbDrop(Connection conn, String name) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String query = String.format("DROP DATABASE %s", name);
            stmt.executeUpdate(query);
        }
    }


}
