package ru.job4j.streams.convert.user;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for User class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 13.02.2018
 */
public class UserTest {

    /**
     * Test getId() method.
     */
    @Test
    public void whenGetIdThenId() {
        int result = new User(23, "Vasya", "Angarsk").getId();
        int expected = 23;
        assertThat(result, is(expected));
    }

    /**
     * Test equals() method.
     */
    @Test
    public void whenEqualElementsThenTrue() {
        User first = new User(65, "Petya", "Helsinki");
        User second = new User(65, "Petya", "Helsinki");
        boolean result = first.equals(second);
        assertThat(result, is(true));
    }

    @Test
    public void whenNotEqualOnlyIdThenFalse() {
        User first = new User(45, "Petya", "Helsinki");
        User second = new User(65, "Petya", "Helsinki");
        boolean result = first.equals(second);
        assertThat(result, is(false));
    }

    @Test
    public void whenNotEqualOnlyNameThenFalse() {
        User first = new User(65, "Anna", "Helsinki");
        User second = new User(65, "Petya", "Helsinki");
        boolean result = first.equals(second);
        assertThat(result, is(false));
    }

    @Test
    public void whenNotEqualOnlyCityThenFalse() {
        User first = new User(65, "Petya", "Turku");
        User second = new User(65, "Petya", "Helsinki");
        boolean result = first.equals(second);
        assertThat(result, is(false));
    }

    /**
     * Test hashcode() method.
     */
    @Test
    public void whenEqualElementsThenEqualHashcode() {
        User first = new User(65, "Petya", "Helsinki");
        User second = new User(65, "Petya", "Helsinki");
        boolean result = first.hashCode() == second.hashCode();
        assertThat(result, is(true));
    }
}