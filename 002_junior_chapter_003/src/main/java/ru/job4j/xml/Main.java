package ru.job4j.xml;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * Main class to run. Calls all actions:
 * 1) Generate values in database.
 * 2) Take database as xml
 * 3) Transform xml format using xsl scheme file.
 * 4) Parse result xml and count sum of 'field' values.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 04.06.2018
 */
public class Main {
    /**
     * Number of values to generate in database.
     */
    public static final int N_VALUES = 10;
    /**
     * Directory where all needed files are stored and generated.
     */
    public static final String RESOURCE_DIR = Paths.get(
            "002_junior_chapter_003", "src", "main", "resources", "ru", "job4j", "xml"
    ).toAbsolutePath().toString();
    /**
     * Path to the database connection config file.
     */
    public static final Path CONFIG = Paths.get(RESOURCE_DIR, "xml_test.properties");
    /**
     * Path to the XSL scheme file for xml transformation.
     */
    public static final Path SCHEME = Paths.get(RESOURCE_DIR, "scheme.xsl");
    /**
     * Path to the generated database file where generated values are stored.
     */
    public static final Path DATABASE = Paths.get(RESOURCE_DIR, "entries.db");
    /**
     * Path to the xml file generated from database.
     */
    public static final Path XML_BEFORE = Paths.get(RESOURCE_DIR, "xml_before.xml");
    /**
     * Path to the transformed xml file.
     */
    public static final Path XML_AFTER = Paths.get(RESOURCE_DIR, "xml_after.xml");

    /**
     * Starts all actions.
     *
     * @param args command-line parameters.
     * @throws IOException                  Signals that an I/O exception of some sort has occurred.
     * @throws SQLException                 Provides information on a database access
     *                                      error or other errors.
     * @throws JAXBException                Shows problems in dumping database to an xml file with JAXB.
     * @throws TransformerException         Shows problems in transforming xml format using XSL.
     * @throws ParserConfigurationException Indicates a serious parser configuration error in parsing xml file.
     * @throws SAXException                 Shows other problems in parsing xml file with SAX.
     */
    public static void main(String[] args)
            throws IOException, SQLException, JAXBException,
            TransformerException, ParserConfigurationException, SAXException {
        Main main = new Main();
        long start = System.currentTimeMillis();
        main.generateDatabase();
        main.databaseToXml();
        main.convertXml();
        String result = main.parseConvertedXml();
        long consumed = System.currentTimeMillis() - start;
        System.out.println();
        System.out.format("= Result: %s%n", result);
        System.out.format("= Time consumed: %d min %d.%03d sec.%n", consumed / 60000, (consumed %= 60000) / 1000, consumed % 1000);
    }

    /**
     * Generates database with values.
     *
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void generateDatabase() throws IOException, SQLException {
        System.out.format("Making DB with %,d values... ", N_VALUES);
        try (StoreSQL store = new StoreSQL(CONFIG, DATABASE)) {
            store.generate(N_VALUES);
        }
        System.out.println("DONE!");
    }

    /**
     * Stores database values to xml.
     *
     * @throws IOException   Signals that an I/O exception of some sort has occurred.
     * @throws SQLException  Provides information on a database access
     *                       error or other errors.
     * @throws JAXBException Shows problems in dumping database to an xml file with JAXB.
     */
    private void databaseToXml() throws IOException, SQLException, JAXBException {
        System.out.print("Saving to xml... ");
        try (StoreXML store = new StoreXML(CONFIG, DATABASE, XML_BEFORE)) {
            store.storeXML();
        }
        System.out.println("DONE!");
    }

    /**
     * Converts xml file to another format.
     *
     * @throws IOException          Signals that an I/O exception of some sort has occurred.
     * @throws TransformerException Shows problems in transforming xml format using XSL.
     */
    private void convertXml() throws IOException, TransformerException {
        System.out.print("Converting xml... ");
        new TransformXSLT().convert(XML_BEFORE, XML_AFTER, SCHEME);
        System.out.println("DONE!");
    }

    /**
     * Parses converted xml and gets result string.
     *
     * @throws IOException                  Signals that an I/O exception of some sort has occurred.
     * @throws ParserConfigurationException Indicates a serious parser configuration error in parsing xml file.
     * @throws SAXException                 Shows other problems in parsing xml file with SAX.
     */
    private String parseConvertedXml() throws ParserConfigurationException, SAXException, IOException {
        System.out.print("Parsing new xml... ");
        String result = new ParseSAX().parse(XML_AFTER);
        System.out.println("DONE!");
        return result;
    }
}
