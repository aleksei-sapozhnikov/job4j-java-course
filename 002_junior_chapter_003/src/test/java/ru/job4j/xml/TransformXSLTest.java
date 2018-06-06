package ru.job4j.xml;

import org.junit.Test;

import javax.xml.transform.TransformerException;
import java.io.IOException;
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
public class TransformXSLTest {
    private final Path config = Paths.get("src/main/resources/ru/job4j/xml/testing.properties").toAbsolutePath();
    private final Path xslScheme;


    public TransformXSLTest() throws IOException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Properties prop = new Properties();
        prop.load(Files.newInputStream(this.config));
        this.xslScheme = Paths.get(this.config.getParent().toString(), prop.getProperty("scheme_file"));
    }

    /**
     * Test generate()
     */
    @Test
    public void whenTransformXmlFormatReturnRightResult() throws IOException, TransformerException {
        // set data
        Path tempDir = Files.createTempDirectory("temp");
        Path source = Files.createTempFile(tempDir, "source.xml", "");
        Path target = Files.createTempFile(tempDir, "converted.xml", "");
        String text = new StringJoiner("\n")
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
        Files.write(source, text.getBytes());
        //transform
        new TransformXSL().convert(source, target, this.xslScheme);
        String result = new String(Files.readAllBytes(target));
        String expected = new StringJoiner(System.lineSeparator())
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
        assertThat(result, is(expected));
    }
}