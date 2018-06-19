package ru.job4j.xml;

import org.junit.Test;
import ru.job4j.common.CommonMethods;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for StoreSQL class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 05.06.2018
 */
public class StoreSQLTest {
    /**
     * Common useful methods.
     */
    private static final CommonMethods METHODS = CommonMethods.getInstance();
    private final Path dbAddress;
    private final String config = "ru/job4j/xml/testing.properties";

    public StoreSQLTest() throws IOException, ClassNotFoundException, URISyntaxException {
        Class.forName("org.sqlite.JDBC");
        Properties prop = METHODS.loadProperties(this, this.config);
        String resDir = Paths.get(
                this.getClass().getResource(".").toURI()
        ).toAbsolutePath().toString();
        this.dbAddress = Paths.get(resDir, prop.getProperty("db_file"));
    }

    /**
     * Test generate()
     */
    @Test
    public void whenGenerateValuesThenFindThemInDatabaseAsNeeded() throws IOException, SQLException {
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
}