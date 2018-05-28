package database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class JDBCExample {
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_PORT_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_NAME = "test_db";
    private static final String DB_URL = DB_PORT_URL.concat(DB_NAME);
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "password";
    private static final Path WORK_PATH = Paths.get(".", "examples", "src", "main", "java", "database");
    private static final Path SQL_FILE_CREATE_TABLES = Paths.get(WORK_PATH.toString(), "create_tables.sql");
    private static final Path SQL_FILE_QUERY = Paths.get(WORK_PATH.toString(), "query.sql");
    private static final Path SQL_FILE_UPDATE = Paths.get(WORK_PATH.toString(), "update.sql");

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Class.forName(DB_DRIVER);
        JDBCExample main = new JDBCExample();
        //main.databaseCreate(DB_NAME);
        //main.databaseDrop(DB_NAME);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
        ) {
            main.dbQueryCreateTablesIfNeeded(stmt);

            //main.dbQueryPerformUpdate(stmt);

            main.dbQueryPerformQuery(stmt);
        }
    }

    private void dbQueryCreateTablesIfNeeded(Statement stmt) throws SQLException, IOException {
        String query = new String(Files.readAllBytes(SQL_FILE_CREATE_TABLES));
        stmt.executeUpdate(query);
    }

    private void dbQueryPerformQuery(Statement stmt) throws IOException, SQLException {
        String query = new String(Files.readAllBytes(SQL_FILE_QUERY));
        try (ResultSet result = stmt.executeQuery(query)) {
            while (result.next()) {
                String s = String.format("%-6s   %-20s", result.getInt("id"), result.getString("login"));
                System.out.println(s);
            }
        }
    }

    private void dbQueryPerformUpdate(Statement stmt) throws SQLException, IOException {
        String query = new String(Files.readAllBytes(SQL_FILE_UPDATE));
        stmt.executeUpdate(query);
    }

    private void databaseCreate(String name) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_PORT_URL, DB_USER, DB_PASSWORD)) {
            this.dbQueryCreateDatabase(conn, name);
        }
    }

    private void databaseDrop(String name) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_PORT_URL, DB_USER, DB_PASSWORD)) {
            this.dbQueryDropDatabase(conn, name);
        }
    }

    private void dbQueryCreateDatabase(Connection conn, String name) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String query = String.format("CREATE DATABASE %s", name);
            stmt.executeUpdate(query);
        }
    }

    private void dbQueryDropDatabase(Connection conn, String name) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String query = String.format("DROP DATABASE %s", name);
            stmt.executeUpdate(query);
        }
    }


}
