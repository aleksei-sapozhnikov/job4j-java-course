package ru.job4j.xml;

import ru.job4j.common.CommonMethods;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Class to store values taken from database to an xml file.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 04.06.2018
 */
public class StoreXML implements AutoCloseable {
    /**
     * Target file where to store resulting xml code.
     */
    private final Path target;
    /**
     * Database connection.
     */
    private final Connection connection;
    /**
     * Common useful methods.
     */
    private static final CommonMethods METHODS = CommonMethods.getInstance();

    /**
     * Constructs new object and connects to database.
     *
     * @param propFile  Path to .properties file with database connection parameters.
     * @param dbAddress Database address (file).
     * @param target    Target file where to store resulting xml code.
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public StoreXML(String propFile, Path dbAddress, Path target) throws IOException, SQLException {
        Properties p = METHODS.loadProperties(this, propFile);
        this.connection = this.dbGetConnection(p, dbAddress);
        this.target = target;
    }

    /**
     * @param properties Path to .properties file with database connection parameters.
     * @param dbAddress  Database address (file).
     * @return Database connection.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private Connection dbGetConnection(Properties properties, Path dbAddress) throws SQLException {
        return DriverManager.getConnection(
                String.format("jdbc:sqlite:%s", dbAddress.toString()),
                properties.getProperty("db_user"),
                properties.getProperty("db_password")
        );
    }

    /**
     * Reads values from database and stores as xml file.
     *
     * @throws JAXBException Shows all JAXB exceptions.
     * @throws IOException   Signals that an I/O exception of some sort has occurred.
     * @throws SQLException  Provides information on a database access
     *                       error or other errors.
     */
    public void storeXML() throws SQLException, JAXBException, IOException {
        Entries source = this.dbGetEntries();
        this.saveToFile(source);
    }

    /**
     * Saves Entries bean object as xml file and writes it to disk.
     *
     * @param source Entries bean object to save.
     * @throws JAXBException Shows all JAXB exceptions.
     * @throws IOException   Signals that an I/O exception of some sort has occurred.
     */
    private void saveToFile(Entries source) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(Entries.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        try (FileOutputStream target = new FileOutputStream(this.target.toString())) {
            marshaller.marshal(source, target);
        }
    }

    /**
     * Reads database and forms Entries bean object with sub-objects.
     *
     * @return Entries bean object.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private Entries dbGetEntries() throws SQLException {
        List<Entry> aEntry = new LinkedList<>();
        String query = "SELECT field FROM entry ORDER BY field";
        try (ResultSet res = this.connection.createStatement().executeQuery(query)) {
            while (res.next()) {
                aEntry.add(new Entry(res.getInt("field")));
            }
        }
        return new Entries(aEntry.toArray(new Entry[0]));
    }

    /**
     * Closes connection. Executed in <tt>try-with-resources</tt> statement.
     *
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    @Override
    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    /**
     * JavaBean class Entries(Entry).
     */
    @XmlRootElement
    private static class Entries {
        private Entry[] entry;

        private Entries() {
        }

        private Entries(Entry... entry) {
            this.entry = entry;
        }

        public Entry[] getEntry() {
            return this.entry;
        }

        public void setEntry(Entry... entry) {
            this.entry = entry;
        }
    }

    /**
     * JavaBean class Entry(field).
     */
    @XmlRootElement
    private static class Entry {
        private int field;

        private Entry() {
        }

        private Entry(int field) {
            this.field = field;
        }

        public int getField() {
            return this.field;
        }

        public void setField(int field) {
            this.field = field;
        }
    }


}
