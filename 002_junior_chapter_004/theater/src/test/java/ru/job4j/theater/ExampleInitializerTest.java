package ru.job4j.theater;

import org.junit.Test;

import java.sql.SQLException;

public class ExampleInitializerTest {

    /**
     * Just runs ExampleInitializer to assure it works.
     *
     * @throws SQLException If problems occur in database.
     */
    @Test
    public void testItWorksAndDoesntThrowExceptions() throws SQLException {
        ExampleInitializer.main(new String[]{"go"});
    }
}