package ru.job4j.xml;

import org.junit.Test;
import org.xml.sax.SAXException;
import ru.job4j.CommonMethods;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.StringJoiner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for TransformXSL class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 06.06.2018
 */
public class ParseSAXTest {
    /**
     * Common useful methods.
     */
    private static final CommonMethods METHODS = new CommonMethods();
    private final Path xslScheme;
    private final String config = "ru/job4j/xml/testing.properties";


    public ParseSAXTest() throws IOException, ClassNotFoundException, URISyntaxException {
        Class.forName("org.sqlite.JDBC");
        Properties prop = METHODS.loadProperties(this, this.config);
        String resDir = Paths.get(
                this.getClass().getResource(".").toURI()
        ).toAbsolutePath().toString();
        this.xslScheme = Paths.get(resDir, prop.getProperty("scheme_file"));
    }

    /**
     * Test generate()
     */
    @Test
    public void whenParseXmlThenReturnNeededResult() throws IOException, TransformerException, ParserConfigurationException, SAXException {
        // set data
        Path tempDir = Files.createTempDirectory("temp");
        Path source = Files.createTempFile(tempDir, "source.xml", "");
        String text = new StringJoiner(System.lineSeparator())
                .add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .add("<entries>")
                .add("    <entry field=\"1\"/>")
                .add("    <entry field=\"2\"/>")
                .add("    <entry field=\"3\"/>")
                .add("    <entry field=\"4\"/>")
                .add("    <entry field=\"5\"/>")
                .add("</entries>")
                .add("")
                .toString();
        Files.write(source, text.getBytes());
        // parse
        String result = new ParseSAX().parse(source);
        String expected = String.format("sum of the \'field\' values = \'%s\'.", 15);
        assertThat(result, is(expected));
    }
}