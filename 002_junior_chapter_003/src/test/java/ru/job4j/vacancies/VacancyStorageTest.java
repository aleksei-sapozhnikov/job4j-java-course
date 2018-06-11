package ru.job4j.vacancies;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VacancyStorageTest {

    public static final String CONFIG = "ru/job4j/vacancies/test.properties";

    private final Properties props = new Properties();

    public VacancyStorageTest() throws IOException {
        Properties props = UsefulMethods.loadProperties(this, CONFIG);
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
    public void whenAddThenFindById() throws IOException, SQLException {
        Vacancy added = new Vacancy("theme", "http://url.java.com", 1220348852437L);
        int id;
        Vacancy found;
        try (VacancyStorage storage = new VacancyStorage(CONFIG, true)) {
            id = storage.add(added);
            found = storage.findById(id);
        }
        assertThat(id, is(found.getId()));
        assertThat(added, is(found));
        System.out.println(id);
    }
}

