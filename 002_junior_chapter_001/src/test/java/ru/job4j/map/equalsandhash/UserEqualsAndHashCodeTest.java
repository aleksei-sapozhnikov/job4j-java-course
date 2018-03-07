package ru.job4j.map.equalsandhash;

import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserEqualsAndHashCodeTest {

    /**
     * Test adding to map
     */
    @Test
    public void whenTwoUsersWithTheSameFieldsAddedToMapThenSecondKeyReplacesFirst() {
        Map<UserEqualsAndHashCode, String> map = new HashMap<>();
        UserEqualsAndHashCode first = new UserEqualsAndHashCode("Ivan", 12, LocalDate.of(1961, 4, 12));
        UserEqualsAndHashCode second = new UserEqualsAndHashCode("Ivan", 12, LocalDate.of(1961, 4, 12));
        assertThat(map.put(first, "first"), is((String) null));
        assertThat(map.put(second, "second"), is("first"));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenStringWithValues() {
        UserEqualsAndHashCode user = new UserEqualsAndHashCode("Ivan", 12, LocalDate.of(1961, 4, 12));
        String result = user.toString();
        String expected = "[name: Ivan, children: 12, birthday: 1961-04-12]";
        assertThat(result, is(expected));
    }

    /**
     * Test equals()
     */
    @Test
    public void whenObjectsWithTheSameFieldsThenEqualsTrue() {
        UserEqualsAndHashCode first = new UserEqualsAndHashCode("Ivan", 12, LocalDate.of(1961, 4, 12));
        UserEqualsAndHashCode second = new UserEqualsAndHashCode("Ivan", 12, LocalDate.of(1961, 4, 12));
        assertThat(first.equals(second), is(true));
    }

    @Test
    public void whenSomeFieldIsNotTheSameThenFalse() {
        UserEquals origin = new UserEquals("Ivan", 12, LocalDate.of(1961, 4, 12));
        UserEquals otherName = new UserEquals("Alena", 12, LocalDate.of(1961, 4, 12));
        UserEquals otherChlidren = new UserEquals("Ivan", 5, LocalDate.of(1961, 4, 12));
        UserEquals otherBirthday = new UserEquals("Ivan", 12, LocalDate.of(2008, 7, 1));
        assertThat(origin.equals(otherName), is(false));
        assertThat(origin.equals(otherChlidren), is(false));
        assertThat(origin.equals(otherBirthday), is(false));
    }

    @Test
    public void whenOtherObjectOfAnotherClassThenEqualsFalse() {
        UserEqualsAndHashCode first = new UserEqualsAndHashCode("Ivan", 12, LocalDate.of(1961, 4, 12));
        String second = "I'm equal!";
        assertThat(first.equals(second), is(false));
    }

    @Test
    public void whenOtherObjectIsNullThenEqualsFalse() {
        UserEqualsAndHashCode first = new UserEqualsAndHashCode("Ivan", 12, LocalDate.of(1961, 4, 12));
        UserEqualsAndHashCode second = null;
        assertThat(first.equals(second), is(false));
    }

    @Test
    public void whenTheSameObjectThenEqualsTrue() {
        UserEqualsAndHashCode first = new UserEqualsAndHashCode("Ivan", 12, LocalDate.of(1961, 4, 12));
        assertThat(first.equals(first), is(true));
    }

    /**
     * Test hashcode()
     */
    @Test
    public void whenObjectsWithTheSameFieldsThenHashCodeTheSame() {
        UserEqualsAndHashCode first = new UserEqualsAndHashCode("Ivan", 12, LocalDate.of(1961, 4, 12));
        UserEqualsAndHashCode second = new UserEqualsAndHashCode("Ivan", 12, LocalDate.of(1961, 4, 12));
        assertThat(first.hashCode() == second.hashCode(), is(true));
    }

}