package ru.job4j.xml;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Conversions {

    public static void main(String[] args) throws IOException, SQLException, JAXBException {
        // make paths
        Path resourcesDir = Paths.get("002_junior_chapter_003", "src", "main", "resources", "ru", "job4j", "xml").toAbsolutePath();
        Path propFile = Paths.get(resourcesDir.toString(), "xml_test.properties");
        Path dbFile = Paths.get(resourcesDir.toString(), "entries.db");
        Path xmlTarget = Paths.get(resourcesDir.toString(), "entries.xml");
        // create values in db
        try (StoreSQL store = new StoreSQL(propFile, dbFile)) {
            store.generate(25);
        }
        // store from db to xml
        try (StoreXML store = new StoreXML(propFile, dbFile, xmlTarget)) {
            store.convert();
        }


    }
}
