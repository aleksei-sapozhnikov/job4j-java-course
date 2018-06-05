package ru.job4j.xml;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args)
            throws IOException, SQLException, JAXBException, TransformerException,
            ParserConfigurationException, SAXException {
        // resources dir
        String root = Paths.get(
                "002_junior_chapter_003", "src", "main", "resources", "ru", "job4j", "xml"
        ).toAbsolutePath().toString();
        // make paths
        Path propFile = Paths.get(root, "xml_test.properties");
        Path dbFile = Paths.get(root, "entries.db");
        Path xmlFirst = Paths.get(root, "entriesFirst.xml");
        Path xmlSecond = Paths.get(root, "entriesSecond.xml");
        Path xslScheme = Paths.get(root, "scheme.xsl");
        // generate values in db
        try (StoreSQL store = new StoreSQL(propFile, dbFile)) {
            store.generate(5);
        }
        // store from db to xml
        try (StoreXML store = new StoreXML(propFile, dbFile, xmlFirst)) {
            store.convert();
        }
        // convert xml with xslt
        new TransformXSLT().convert(xmlFirst, xmlSecond, xslScheme);
        // parse converted xml
        int fieldSum = new ParseSAX().parseFieldSum(xmlSecond);
        System.out.format("\"field\" values sum = %s", fieldSum);
    }
}
