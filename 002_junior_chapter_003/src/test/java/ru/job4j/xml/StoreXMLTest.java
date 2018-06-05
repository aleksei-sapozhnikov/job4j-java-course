package ru.job4j.xml;

import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
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
    private final Path config = Paths.get("src/main/resources/ru/job4j/xml/testing.properties").toAbsolutePath();
    private final Path dbAddress;
    private final Path target;

    public StoreXMLTest() throws IOException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Properties prop = new Properties();
        prop.load(Files.newInputStream(this.config));
        this.dbAddress = Paths.get(config.getParent().toString(), prop.getProperty("db_file"));
        this.target = Paths.get(config.getParent().toString(), prop.getProperty("xml_before"));
    }

    /**
     * Test store()
     */
    @Test
    public void whenStoreGeneratedValuesFromDatabaseThenXmlFileAsNeeded() throws IOException, SQLException, JAXBException {
        try (StoreSQL store = new StoreSQL(this.config, this.dbAddress)) {
            store.generate(5);
        }
        try (StoreXML store = new StoreXML(this.config, this.dbAddress, this.target)) {
            store.storeXML();
        }
        String result = new String(Files.readAllBytes(this.target));
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