package ru.job4j.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class StoreXML implements AutoCloseable {
    private final Path target;
    private final Connection connection;

    public StoreXML(Path propertiesFile, Path dbAddress, Path target) throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(new FileReader(propertiesFile.toString()));
        this.connection = this.dbGetConnection(properties, dbAddress);
        this.target = target;
    }

    private Connection dbGetConnection(Properties properties, Path dbAddress) throws SQLException {
        String url = String.format(
                "jdbc:%s:%s",
                properties.getProperty("db_type"), dbAddress.toString()
        );
        return DriverManager.getConnection(
                url,
                properties.getProperty("db_user"),
                properties.getProperty("db_password")
        );
    }

    private void save(Entries source) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(Entries.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        try (FileOutputStream target = new FileOutputStream(this.target.toString())) {
            marshaller.marshal(source, target);
        }
    }

    public void convert() throws SQLException, JAXBException, IOException {
        Entries source = this.dbGetEntries();
        this.save(source);
    }

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

    @Override
    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }

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
