package ru.job4j.xml;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertArrayEquals;

public class StoreSQLTest {
    private final Path config = Paths.get("src/main/resources/ru/job4j/xml/testing.properties").toAbsolutePath();
    private final Path dbAddress;
    private final Properties prop = new Properties();


    public StoreSQLTest() throws IOException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        prop.load(Files.newInputStream(this.config));
        this.dbAddress = Paths.get(config.getParent().toString(), prop.getProperty("db_file"));
    }

    /**
     * Test generate()
     */
    @Test
    public void generate() throws IOException, SQLException {
        try (StoreSQL store = new StoreSQL(this.config, this.dbAddress)) {
            store.generate(5);
        }
        int[] result = new int[5];
        try (ResultSet res = DriverManager.getConnection(
                String.format("jdbc:sqlite:%s", dbAddress.toString())
        ).createStatement().executeQuery(
                "SELECT field FROM entry"
        )) {
            int i = 0;
            while (res.next()) {
                result[i++] = res.getInt("field");
            }
        }
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(result, expected);
    }

    @Test
    public void close() {
    }
}