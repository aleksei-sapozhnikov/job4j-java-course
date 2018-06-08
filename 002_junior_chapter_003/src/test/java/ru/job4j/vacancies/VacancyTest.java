package ru.job4j.vacancies;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.StringJoiner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VacancyTest {

    /**
     * Test constrictors and getters.
     */
    @Test
    public void whenGetterThenValue() {
        // with given id
        Vacancy givenId = new Vacancy(32, "first", "http://first.com", 133456789);
        assertThat(givenId.getId(), is(32));
        assertThat(givenId.getTitle(), is("first"));
        assertThat(givenId.getUrl(), is("http://first.com"));
        assertThat(givenId.getUpdated(), is(133456789L));
        // with default id = -1
        Vacancy defaultId = new Vacancy("second", "http://second.com", 987654321);
        assertThat(defaultId.getId(), is(-1));
        assertThat(defaultId.getTitle(), is("second"));
        assertThat(defaultId.getUrl(), is("http://second.com"));
        assertThat(defaultId.getUpdated(), is(987654321L));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenStringAsNeeded() {
        Vacancy vacancy = new Vacancy(32, "first", "http://first.com", 133456789);
        String result = vacancy.toString();
        String expected = new StringJoiner(System.lineSeparator())
                .add(Integer.toString(32)
                        .concat(" : ")
                        .concat(LocalDateTime.ofInstant(Instant.ofEpochMilli(133456789), Vacancy.ZONE_ID).toString()))
                .add("first")
                .add("http://first.com")
                .add("")
                .toString();
        assertThat(result, is(expected));

    }

    /**
     * Test equals() and hashCode()
     */
    @Test
    public void testEqualsVariantsAndHashcode() {
        Vacancy main = new Vacancy(12, "main", "http://url", 435);
        // Vacancies to compare
        Vacancy itself = main;
        Vacancy same = new Vacancy(12, "main", "http://url", 435);
        Vacancy otherId = new Vacancy(99, "main", "http://url", 435);
        Vacancy otherName = new Vacancy(12, "other", "http://url", 435);
        Vacancy otherUrl = new Vacancy(12, "main", "http://other", 435);
        Vacancy otherUpdated = new Vacancy(12, "main", "http://url", 987);
        String otherClass = "I'm the Vacancy!";
        Vacancy nullVacancy = null;
        // equal
        assertThat(main.equals(itself), is(true));
        assertThat(main.equals(same), is(true));
        assertThat(main.equals(otherId), is(true));
        // not equal
        assertThat(main.equals(otherName), is(false));
        assertThat(main.equals(otherUrl), is(false));
        assertThat(main.equals(otherUpdated), is(false));
        assertThat(main.equals(otherClass), is(false));
        assertThat(main.equals(nullVacancy), is(false));
        // hashcode of equal
        assertThat(main.hashCode() == itself.hashCode(), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
        assertThat(main.hashCode() == otherId.hashCode(), is(true));
    }
}