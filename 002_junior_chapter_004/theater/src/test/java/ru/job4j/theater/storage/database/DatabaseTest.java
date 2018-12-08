package ru.job4j.theater.storage.database;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DatabaseTest {

    @Test
    public void whenQueryNotFoundThenException() {
        boolean caught = false;
        try {
            Database.getInstance().getQuery("blablabla");
        } catch (SqlQueryNotFoundException e) {
            caught = true;
        }
        assertThat(caught, is(true));
    }
}