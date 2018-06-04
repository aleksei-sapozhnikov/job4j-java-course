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

/**
 * Class to parse xml file and calculate values.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 04.06.2018
 */
public class ParseSAX extends DefaultHandler {
    /**
     * Sum value.
     */
    private long sum;

    /**
     * Parses given xml file and prints sum of "field" values.
     *
     * @param source xml file to parse.
     * @return String describing result.
     * @throws IOException                  Signals that an I/O exception of some sort has occurred.
     * @throws SAXException                 Shows other problems in parsing xml file with SAX.
     * @throws ParserConfigurationException Indicates a serious parser configuration error in parsing xml file.
     */
    public String parse(Path source) throws IOException, SAXException, ParserConfigurationException {
        XMLReader reader = this.getXMLReader();
        reader.setContentHandler(this);
        this.sum = 0;
        reader.parse(new InputSource(Files.newInputStream(source)));
        return String.format("sum of the \'field\' values = \'%s\'.", this.sum);
    }

    /**
     * Returns xml file reader. The reader uses "this" object as a handler.
     *
     * @return xml file reader.
     * @throws SAXException                 Shows other problems in parsing xml file with SAX.
     * @throws ParserConfigurationException Indicates a serious parser configuration error in parsing xml file.
     */
    private XMLReader getXMLReader() throws ParserConfigurationException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        return saxParser.getXMLReader();
    }

    /**
     * Executed at the start of an element. If the element is
     * as needed ("entry"), reads its "field" attribute and
     * adds it to the sum value.
     *
     * @param uri        The Namespace URI, or the empty string if the
     *                   element has no Namespace URI or if Namespace
     *                   processing is not being performed.
     * @param localName  The local name (without prefix), or the
     *                   empty string if Namespace processing is not being
     *                   performed.
     * @param qName      The qualified name (with prefix), or the
     *                   empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If
     *                   there are no attributes, it shall be an empty
     *                   Attributes object.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (localName.equals("entry")) {
            int i = Integer.valueOf(attributes.getValue("field"));
            this.sum += i;
        }
    }
}
