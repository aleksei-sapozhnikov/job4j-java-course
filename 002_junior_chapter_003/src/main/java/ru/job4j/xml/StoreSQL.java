package ru.job4j.xml;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class StoreSQL implements AutoCloseable {
    private final Connection connection;

    public StoreSQL(Path propertiesFile, Path dbAddress) throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(new FileReader(propertiesFile.toString()));
        this.connection = this.dbGetConnection(properties, dbAddress);
    }

    private Connection dbGetConnection(Properties properties, Path dbAddress) throws SQLException {
        String url = String.format(
                "jdbc:%s:%s",
                properties.getProperty("db_type"), dbAddress.toString()
        );
        return DriverManager.getConnection(
                url,
                properties.getProperty("db_user"),
                properties.getProperty("db_password")
        );
    }

    public void generate(int n) throws SQLException {
        this.dbPerformUpdate("CREATE TABLE IF NOT EXISTS entry (field INTEGER PRIMARY KEY)");
        this.dbPerformUpdate("DELETE FROM entry");
        for (int i = 1; i <= n; i++) {
            this.dbPerformUpdate(String.format("INSERT INTO entry (field) VALUES (%s)", i));
        }
    }

    private void dbPerformUpdate(String query) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(query);
        }
    }

    @Override
    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }
}
