package ru.job4j.xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParseSAX extends DefaultHandler {
    private int sum = 0;

    public int parseFieldSum(Path source) throws IOException, SAXException, ParserConfigurationException {
        XMLReader reader = this.getXMLReader();
        reader.setContentHandler(this);
        reader.parse(new InputSource(Files.newInputStream(source)));
        return this.sum;
    }

    private XMLReader getXMLReader() throws ParserConfigurationException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        return saxParser.getXMLReader();
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) {
        if (localName.equals("entry")) {
            int i = Integer.valueOf(attrs.getValue("field"));
            this.sum += i;
        }
    }
}
