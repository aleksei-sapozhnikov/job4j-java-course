package ru.job4j.vacancies;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;

public class VacancyParserTest {
    public static final String TEST_1 = "ru/job4j/vacancies/parser_sample_1.htm";


    @Test
    public void whenParseSampleFileGetsResultAsNeeded() throws IOException, ParseException, URISyntaxException {
        ClassLoader loader = VacancyParserTest.class.getClassLoader();
        String content;
        try (InputStream stream = loader.getResourceAsStream(TEST_1)) {
            content = this.inputStreamToString(stream);
        }
        VacancyParser parser = new VacancyParser();
        Vacancy a = parser.parseString(content).get(0);
        System.out.println(a.getId());
        System.out.println(a.getTheme());
        System.out.println(a.getUrl());
        System.out.println(a.getPublished());

    }


    private String inputStreamToString(InputStream stream) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            bytes.write(buffer, 0, length);
        }
        return bytes.toString("windows-1251");
    }


}