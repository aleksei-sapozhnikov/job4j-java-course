package ru.job4j.xml;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class to convert xml file to another format using XSL.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 04.06.2018
 */
public class TransformXSL {

    /**
     * @param source XML file to transform.
     * @param dest   where to store transformed xml text.
     * @param scheme XSL scheme describing transformation.
     * @throws IOException          Signals that an I/O exception of some sort has occurred.
     * @throws TransformerException Shows that an exceptional condition that occurred
     *                              during the transformation process.
     */
    public void convert(Path source, Path dest, Path scheme) throws IOException, TransformerException {
        Transformer transformer = this.getTransformer(scheme);
        transformer.transform(
                new StreamSource(Files.newInputStream(source)),
                new StreamResult(Files.newOutputStream(dest))
        );
    }

    /**
     * Returns new XML transformer working by given XSL scheme.
     *
     * @param scheme XSL scheme.
     * @return New XML format transformer.
     * @throws IOException                       Signals that an I/O exception of some sort has occurred.
     * @throws TransformerConfigurationException Indicates a serious configuration error.
     */
    private Transformer getTransformer(Path scheme) throws IOException, TransformerConfigurationException {
        String text = new String(Files.readAllBytes(scheme));
        StreamSource source = new StreamSource(new ByteArrayInputStream(text.getBytes()));
        TransformerFactory factory = TransformerFactory.newInstance();
        return factory.newTransformer(source);
    }

}

