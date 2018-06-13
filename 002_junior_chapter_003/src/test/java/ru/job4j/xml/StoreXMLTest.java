package ru.job4j.xml;

import org.junit.Test;
import ru.job4j.CommonMethods;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringJoiner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for StoreXML class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 05.06.2018
 */
public class StoreXMLTest {
    /**
     * Common useful methods.
     */
    private static final CommonMethods METHODS = new CommonMethods();
    private final Path dbAddress;
    private final String config = "ru/job4j/xml/testing.properties";

    public StoreXMLTest() throws IOException, ClassNotFoundException, URISyntaxException {
        Class.forName("org.sqlite.JDBC");
        Properties prop = METHODS.loadProperties(this, this.config);
        String resDir = Paths.get(
                this.getClass().getResource(".").toURI()
        ).toAbsolutePath().toString();
        this.dbAddress = Paths.get(resDir, prop.getProperty("db_file"));
    }

    /**
     * Test store()
     */
    @Test
    public void whenStoreGeneratedValuesFromDatabaseThenXmlFileAsNeeded() throws IOException, SQLException, JAXBException {
        Path tempDir = Files.createTempDirectory("temp");
        Path target = Files.createTempFile(tempDir, "target.xml", "");
        try (StoreSQL store = new StoreSQL(this.config, this.dbAddress)) {
            store.generate(5);
        }
        try (StoreXML store = new StoreXML(this.config, this.dbAddress, target)) {
            store.storeXML();
        }
        String result = new String(Files.readAllBytes(target));
        String expected = new StringJoiner("\n")
                .add("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
                .add("<entries>")
                .add("    <entry>")
                .add("        <field>1</field>")
                .add("    </entry>")
                .add("    <entry>")
                .add("        <field>2</field>")
                .add("    </entry>")
                .add("    <entry>")
                .add("        <field>3</field>")
                .add("    </entry>")
                .add("    <entry>")
                .add("        <field>4</field>")
                .add("    </entry>")
                .add("    <entry>")
                .add("        <field>5</field>")
                .add("    </entry>")
                .add("</entries>")
                .add("")
                .toString();
        assertThat(result, is(expected));
    }

}