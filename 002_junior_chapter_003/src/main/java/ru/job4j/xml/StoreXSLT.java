package ru.job4j.xml;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StoreXSLT {
    private final Path source;
    private final Path dest;
    private final Path scheme;


    public StoreXSLT(Path source, Path dest, Path scheme) {
        this.source = source;
        this.dest = dest;
        this.scheme = scheme;
    }

    public void convert() throws IOException, TransformerException {
        String schemeText = new String(Files.readAllBytes(this.scheme));
        String sourceText = new String(Files.readAllBytes(this.source));

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(
                new StreamSource(
                        new ByteArrayInputStream(schemeText.getBytes()))
        );
        transformer.transform(
                new StreamSource(new ByteArrayInputStream(sourceText.getBytes())),
                new StreamResult(this.dest.toFile())
        );
    }
}

