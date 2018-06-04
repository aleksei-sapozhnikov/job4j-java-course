package ru.job4j.xml;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Main class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 06.06.2018
 */
public class MainTest {
    private final Path config = Paths.get("src/main/resources/ru/job4j/xml/testing.properties").toAbsolutePath();
    private final int nValues;
    private final Path database;
    private final Path xmlBefore;
    private final Path xmlAfter;


    public MainTest() throws IOException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Properties prop = new Properties();
        prop.load(Files.newInputStream(this.config));
        String dir = config.getParent().toAbsolutePath().toString();
        this.database = Paths.get(dir, prop.getProperty("db_file"));
        this.xmlBefore = Paths.get(dir, prop.getProperty("xml_before"));
        this.xmlAfter = Paths.get(dir, prop.getProperty("xml_after"));
        this.nValues = Integer.valueOf(prop.getProperty("values"));
    }

    /**
     * Test generate()
     */
    @Test
    public void whenDoAllActionsThenGetResultAsNeeded() throws IOException, SQLException, JAXBException, TransformerException, ParserConfigurationException, SAXException {
        // change System.out
        PrintStream stdout = System.out;
        String[] result;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            // work
            Main.main(new String[]{this.config.toString()});
            String output = new String(out.toByteArray());
            result = output.split(System.lineSeparator());
        } finally {
            // return System.out
            System.setOut(stdout);
        }
        assertThat(result[0], is(String.format("Making DB with %,d values... DONE!", this.nValues)));
        assertThat(result[1], is("Saving to xml... DONE!"));
        assertThat(result[2], is("Converting xml... DONE!"));
        assertThat(result[3], is("Parsing new xml... DONE!"));
        assertThat(result[4], is(""));
        assertThat(result[5], is(String.format("= Result: sum of the 'field' values = \'%,d\'.", this.calculateSum(this.nValues))));
    }

    /**
     * Returns sum of values from 1 to nValues.
     *
     * @param nValues max number.
     * @return sum.
     */
    private long calculateSum(int nValues) {
        long sum = 0;
        for (int i = 1; i <= nValues; i++) {
            sum += i;
        }
        return sum;
    }
}