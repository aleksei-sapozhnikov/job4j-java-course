package ru.job4j.xml;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

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
     * Path to the default config file.
     */
    public static final Path DEFAULT_CONFIG = Paths.get(
            "002_junior_chapter_003/src/main/resources/ru/job4j/xml/main.properties"
    ).toAbsolutePath();
    /**
     * Path to the config file.
     */
    private final Path config;
    /**
     * Path to the XSL scheme file for xml transformation.
     */
    private final Path scheme;
    /**
     * Path to the generated database file where generated values are stored.
     */
    private final Path database;
    /**
     * Path to the xml file generated from database.
     */
    private final Path xmlBefore;
    /**
     * Path to the transformed xml file.
     */
    private final Path xmlAfter;
    /**
     * Number of values to generate in database.
     */
    private final int nValues;


    public Main(Path config) throws IOException {
        Properties prop = new Properties();
        prop.load(Files.newInputStream(config));
        String dir = config.getParent().toAbsolutePath().toString();
        this.config = config;
        this.database = Paths.get(dir, prop.getProperty("database"));
        this.scheme = Paths.get(dir, prop.getProperty("scheme"));
        this.xmlBefore = Paths.get(dir, prop.getProperty("xml_before"));
        this.xmlAfter = Paths.get(dir, prop.getProperty("xml_after"));
        this.nValues = Integer.valueOf(prop.getProperty("values"));
    }

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
        Main main = new Main(
                args.length <= 0
                        ? DEFAULT_CONFIG
                        : Paths.get(args[0]).toAbsolutePath()
        );
        long start = System.currentTimeMillis();
        main.generateDatabase();
        main.databaseToXml();
        main.convertXml();
        String result = main.parseConverted();
        long consumed = System.currentTimeMillis() - start;
        System.out.println();
        System.out.println(main.printResult(result, consumed));
    }

    /**
     * Generates database with values.
     *
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void generateDatabase() throws IOException, SQLException {
        System.out.format("Making DB with %,d values... ", this.nValues);
        try (StoreSQL store = new StoreSQL(this.config, this.database)) {
            store.generate(nValues);
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
        try (StoreXML store = new StoreXML(this.config, this.database, this.xmlBefore)) {
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
        new TransformXSLT().convert(this.xmlBefore, this.xmlAfter, this.scheme);
        System.out.println("DONE!");
    }

    /**
     * Parses converted xml and gets result string.
     *
     * @throws IOException                  Signals that an I/O exception of some sort has occurred.
     * @throws ParserConfigurationException Indicates a serious parser configuration error in parsing xml file.
     * @throws SAXException                 Shows other problems in parsing xml file with SAX.
     */
    private String parseConverted() throws ParserConfigurationException, SAXException, IOException {
        System.out.print("Parsing new xml... ");
        String result = new ParseSAX().parse(this.xmlAfter);
        System.out.println("DONE!");
        return result;
    }

    /**
     * Returns string with formatted program work result.
     *
     * @param result result of the work.
     * @param time   time consumed in milliseconds
     * @return Formatted result string.
     */
    private String printResult(String result, long time) {
        long min = time / 60_000;
        time %= 60_000;
        long sec = time / 1000;
        long mils = time % 1000;
        return String.format(
                "= Result: %s%n", result
        ).concat(String.format(
                "= Time consumed: %d min %d.%03d sec.%n", min, sec, mils
        ));
    }
}
