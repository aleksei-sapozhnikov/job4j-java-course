package ru.job4j.xml;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Conversions {

    public static void main(String[] args) throws IOException, SQLException, JAXBException, TransformerException {
        // make paths
        Path root = Paths.get("002_junior_chapter_003", "src", "main", "resources", "ru", "job4j", "xml").toAbsolutePath();
        Path propFile = Paths.get(root.toString(), "xml_test.properties");
        Path dbFile = Paths.get(root.toString(), "entries.db");
        Path entriesFi = Paths.get(root.toString(), "entriesFirst.xml");
        Path xmlSecond = Paths.get(root.toString(), "entriesSecond.xml");
        Path xslScheme = Paths.get(root.toString(), "scheme.xsl");

        // create values in db
        try (StoreSQL store = new StoreSQL(propFile, dbFile)) {
            store.generate(10);
        }
        // store from db to xml
        try (StoreXML store = new StoreXML(propFile, dbFile, entriesFi)) {
            store.convert();
        }
        // convert xml with xslt
        {
            StoreXSLT convert = new StoreXSLT(entriesFi, xmlSecond, xslScheme);
            convert.convert();
        }
    }
}
