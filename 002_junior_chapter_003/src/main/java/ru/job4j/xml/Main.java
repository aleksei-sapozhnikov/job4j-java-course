package ru.job4j.xml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import ru.job4j.common.CommonMethods;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URISyntaxException;
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
    public static final String DEFAULT_CONFIG = "ru/job4j/xml/main.properties";
    /**
     * Common useful methods.
     */
    private static final CommonMethods METHODS = CommonMethods.getInstance();
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Main.class);
    /**
     * Path to the config file. Loaded by Classloader.
     */
    private final String config;
    /**
     * Path to the XSL scheme file for xml transformation. Loaded by Classloader.
     */
    private final Path scheme;
    /**
     * Path to the generated database file where generated values are stored. Loaded by Classloader.
     */
    private final Path database;
    /**
     * Path to the xml file generated from database. Loaded by Classloader.
     */
    private final Path xmlBefore;
    /**
     * Path to the transformed xml file. Loaded by Classloader.
     */
    private final Path xmlAfter;
    /**
     * Number of values to generate in database. Loaded by Classloader.
     */
    private final int nValues;

    /**
     * Constructs new Main() object running all processes.
     *
     * @param config Config file. Loaded by Classloader.
     *               Format like: "ru/job4j/xml/main.properties".
     * @throws IOException If encountered problems in loading properties.
     */
    public Main(String config) throws IOException, URISyntaxException {
        Properties prop = METHODS.loadProperties(this, config);
        String resDir = Paths.get(
                this.getClass().getResource(".").toURI()
        ).toAbsolutePath().toString();
        this.config = config;
        this.database = Paths.get(resDir, prop.getProperty("db_file"));
        this.scheme = Paths.get(resDir, prop.getProperty("scheme_file"));
        this.xmlBefore = Paths.get(resDir, prop.getProperty("xml_before"));
        this.xmlAfter = Paths.get(resDir, prop.getProperty("xml_after"));
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
            TransformerException, ParserConfigurationException, SAXException, URISyntaxException {
        Main main = new Main(
                args.length <= 0
                        ? DEFAULT_CONFIG
                        : args[0]
        );
        long start = System.currentTimeMillis();
        main.generateDatabase();
        main.databaseToXml();
        main.convertXml();
        String result = main.parseConverted();
        long consumed = System.currentTimeMillis() - start;
        LOG.info(main.printResult(result, consumed));
    }

    /**
     * Generates database with values.
     *
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void generateDatabase() throws IOException, SQLException {
        LOG.info(String.format("Making DB with %,d values... ", this.nValues));
        try (StoreSQL store = new StoreSQL(this.config, this.database)) {
            store.generate(nValues);
        }
        LOG.info("DONE! Database generated!");
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
        LOG.info("Saving to xml... ");
        try (StoreXML store = new StoreXML(this.config, this.database, this.xmlBefore)) {
            store.storeXML();
        }
        LOG.info("DONE! Xml saved!");
    }

    /**
     * Converts xml file to another format.
     *
     * @throws IOException          Signals that an I/O exception of some sort has occurred.
     * @throws TransformerException Shows problems in transforming xml format using XSL.
     */
    private void convertXml() throws IOException, TransformerException {
        LOG.info("Converting xml... ");
        new TransformXSL().convert(this.xmlBefore, this.xmlAfter, this.scheme);
        LOG.info("DONE! Xml converted!");
    }

    /**
     * Parses converted xml and gets result string.
     *
     * @throws IOException                  Signals that an I/O exception of some sort has occurred.
     * @throws ParserConfigurationException Indicates a serious parser configuration error in parsing xml file.
     * @throws SAXException                 Shows other problems in parsing xml file with SAX.
     */
    private String parseConverted() throws ParserConfigurationException, SAXException, IOException {
        LOG.info("Parsing new xml... ");
        String result = new ParseSAX().parse(this.xmlAfter);
        LOG.info("DONE! Xml parsing complete!");
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
                "== RESULT: %s Time consumed: %d min %d.%03d sec.",
                result, min, sec, mils);
    }
}
