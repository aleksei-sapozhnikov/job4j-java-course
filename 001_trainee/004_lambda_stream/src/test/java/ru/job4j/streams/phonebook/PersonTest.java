package ru.job4j.streams.phonebook;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Person class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.02.2018
 */
public class PersonTest {

    /**
     * Test containsInAnyField() method.
     */
    @Test
    public void whenContainsKeyInAnyFieldThenTrue() {
        Person person = new Person("Ivan", "Demidov", "12345678", "Moscow");
        boolean result = person.containsInAnyField("Demi");
        assertThat(result, is(true));
    }

    @Test
    public void whenNotContainsKeyInAnyFieldThenFalse() {
        Person person = new Person("Ivan", "Demidov", "12345678", "Moscow");
        boolean result = person.containsInAnyField("Kozlov");
        assertThat(result, is(false));
    }

    /**
     * Test containsInAnyFieldReflectAPI() method.
     */
    @Test
    public void whenContainsKeyInAnyFieldThenTrueReflectAPI() {
        Person person = new Person("Ivan", "Demidov", "12345678", "Moscow");
        boolean result = person.containsInAnyFieldReflectAPI("Demi");
        assertThat(result, is(true));
    }

    @Test
    public void whenNotContainsKeyInAnyFieldThenFalseReflectAPI() {
        Person person = new Person("Ivan", "Demidov", "12345678", "Moscow");
        boolean result = person.containsInAnyFieldReflectAPI("Kozlov");
        assertThat(result, is(false));
    }

    /**
     * Test equals() method.
     */
    @Test
    public void whenEqualPersonsThenTrue() {
        Person left = new Person("Ivan", "Demidov", "12345678", "Moscow");
        Person right = new Person("Ivan", "Demidov", "12345678", "Moscow");
        boolean result = left.equals(right);
        assertThat(result, is(true));
    }

    @Test
    public void whenNotEqualPersonsThenFalse() {
        Person left = new Person("Grigory", "Demidov", "12345678", "Moscow");
        Person right = new Person("Ivan", "Demidov", "12345678", "Moscow");
        boolean result = left.equals(right);
        assertThat(result, is(false));
    }

    @Test
    public void whenTheSameObjectThenEqualsTrue() {
        Person person = new Person("Grigory", "Demidov", "12345678", "Moscow");
        boolean result = person.equals(person);
        assertThat(result, is(true));
    }

    @Test
    public void whenDifferentClassThenThenEqualsFalse() {
        Person person = new Person("Grigory", "Demidov", "12345678", "Moscow");
        String other = "Person";
        boolean result = person.equals(other);
        assertThat(result, is(false));
    }

    @Test
    public void whenNullThenEqualsFalse() {
        Person left = new Person("Grigory", "Demidov", "12345678", "Moscow");
        Person right = null;
        boolean result = left.equals(right);
        assertThat(result, is(false));
    }

    /**
     * Test hashcode() method.
     */
    @Test
    public void whenEqualPersonsThenHashCodeTheSame() {
        Person left = new Person("Ivan", "Demidov", "12345678", "Moscow");
        Person right = new Person("Ivan", "Demidov", "12345678", "Moscow");
        boolean result = left.hashCode() == right.hashCode();
        assertThat(result, is(true));
    }
}