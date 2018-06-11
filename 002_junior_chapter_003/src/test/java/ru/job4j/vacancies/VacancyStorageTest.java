package ru.job4j.vacancies;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class VacancyStorageTest {

    public static final String TEST_CONFIG = "ru/job4j/vacancies/test.properties";

    private final Properties props = new Properties();

    public VacancyStorageTest() throws IOException {
        Properties props = this.loadProperties(TEST_CONFIG);
    }

    private Properties loadProperties(String config) throws IOException {
        Properties props = new Properties();
        ClassLoader loader = VacancyStorage.class.getClassLoader();
        try (InputStream input = loader.getResourceAsStream(config)) {
            props.load(input);
        }
        return props;
    }

    @Test
    public void whenConnect() throws IOException, SQLException {
        try (VacancyStorage storage = new VacancyStorage(TEST_CONFIG)) {
            System.out.println("inside");
        }
    }

}