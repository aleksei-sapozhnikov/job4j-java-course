package ru.job4j.map.equalsandhash;

import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserEqualsTest {

    /**
     * Test adding to map
     */
    @Test
    public void whenTwoUsersWithTheSameFieldsAddedToMapThenTwoKeys() {
        Map<UserEquals, String> map = new HashMap<>();
        UserEquals first = new UserEquals("Ivan", 12, LocalDate.of(1961, 4, 12));
        UserEquals second = new UserEquals("Ivan", 12, LocalDate.of(1961, 4, 12));
        assertThat(map.put(first, "first"), is((String) null));
        assertThat(map.put(second, "second"), is((String) null));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenStringWithValues() {
        UserEquals user = new UserEquals("Ivan", 12, LocalDate.of(1961, 4, 12));
        String result = user.toString();
        String expected = "[name: Ivan, children: 12, birthday: 1961-04-12]";
        assertThat(result, is(expected));
    }

    /**
     * Test equals()
     */
    @Test
    public void whenObjectsWithTheSameFieldsThenEqualsTrue() {
        UserEquals first = new UserEquals("Ivan", 12, LocalDate.of(1961, 4, 12));
        UserEquals second = new UserEquals("Ivan", 12, LocalDate.of(1961, 4, 12));
        assertThat(first.equals(second), is(true));
    }

    @Test
    public void whenOtherObjectOfAnotherClassThenEqualsFalse() {
        UserEquals first = new UserEquals("Ivan", 12, LocalDate.of(1961, 4, 12));
        String second = "I'm equal!";
        assertThat(first.equals(second), is(false));
    }

    @Test
    public void whenOtherObjectIsNullThenEqualsFalse() {
        UserEquals first = new UserEquals("Ivan", 12, LocalDate.of(1961, 4, 12));
        UserEquals second = null;
        assertThat(first.equals(second), is(false));
    }

    @Test
    public void whenTheSameObjectThenEqualsTrue() {
        UserEquals first = new UserEquals("Ivan", 12, LocalDate.of(1961, 4, 12));
        assertThat(first.equals(first), is(true));
    }

}