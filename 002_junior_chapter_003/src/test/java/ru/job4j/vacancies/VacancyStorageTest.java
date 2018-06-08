package ru.job4j.vacancies;

import org.junit.Test;
import ru.job4j.CommonMethods;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VacancyStorageTest {

    public static final CommonMethods METHODS = new CommonMethods();

    public static final String CONFIG = "ru/job4j/vacancies/test.properties";

    private final Properties props = new Properties();

    public VacancyStorageTest() throws IOException {
        Properties props = METHODS.loadProperties(this, CONFIG);
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
    }

    @Test
    public void whenAddThenFindIdByTitleAndUrl() throws IOException, SQLException {
        Vacancy added = new Vacancy("theme", "http://url.java.com", 1220348852437L);
        String title = "theme";
        String url = "http://url.java.com";
        int idAdded;
        int idFound;
        try (VacancyStorage storage = new VacancyStorage(CONFIG, true)) {
            idAdded = storage.add(added);
            idFound = storage.findIdByTitleAndUrl(title, url);
        }
        assertThat(idAdded, is(idFound));
    }

    @Test
    public void whenUpdateAddedThenNewValues() throws IOException, SQLException {
        Vacancy adding = new Vacancy("theme", "http://url.java.com", 1220348852437L);
        Vacancy update = new Vacancy("another", "http://another.url", 2322323423432L);
        Vacancy updated;
        int id;
        try (VacancyStorage storage = new VacancyStorage(CONFIG, true)) {
            id = storage.add(adding);
            storage.update(id, update);
            updated = storage.findById(id);
        }
        assertThat(updated.getId(), is(id));
        assertThat(updated, is(update));
    }


    @Test
    public void whenAddVacanciesTheyAraAddedOrUpdatedIfExist() throws IOException, SQLException {
        Vacancy one = new Vacancy("theme_1", "url_1", 123);
        Vacancy two = new Vacancy("theme_2", "url_2", 456);
        Vacancy three = new Vacancy("theme_2", "url_2", 789);   // update two
        Vacancy four = new Vacancy("theme_3", "url_3", 321);
        int idOne, idTwo, idThree, idFour;
        Vacancy foundOne, foundTwo, foundThree, foundFour;
        try (VacancyStorage storage = new VacancyStorage(CONFIG, true)) {
            idOne = storage.addOrUpdate(one);       // add
            idTwo = storage.addOrUpdate(two);       // add
            idThree = storage.addOrUpdate(three);       // update
            idFour = storage.addOrUpdate(four);     // add
            foundOne = storage.findById(idOne);
            foundTwo = storage.findById(idTwo);
            foundThree = storage.findById(idThree);
            foundFour = storage.findById(idFour);
        }
        // added
        assertThat(one, is(foundOne));
        assertThat(three, is(foundThree));
        assertThat(four, is(foundFour));
        // updated
        assertThat(idTwo, is(idThree));
        assertThat(foundTwo, is(foundThree));
    }

}

