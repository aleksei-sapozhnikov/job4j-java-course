package ru.job4j.map.equalsandhash;

import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserHashcodeTest {

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenStringWithValues() {
        UserHashcode user = new UserHashcode("Ivan", 12, LocalDate.of(1961, 4, 12));
        String result = user.toString();
        String expected = "[name: Ivan, children: 12, birthday: 1961-04-12]";
        assertThat(result, is(expected));
    }

    /**
     * Test hashcode()
     */
    @Test
    public void whenObjectsWithTheSameFieldsThenHashCodeTheSame() {
        UserHashcode first = new UserHashcode("Ivan", 12, LocalDate.of(1961, 4, 12));
        UserHashcode second = new UserHashcode("Ivan", 12, LocalDate.of(1961, 4, 12));
        assertThat(first.hashCode() == second.hashCode(), is(true));
    }

    /**
     * Test adding to map
     */
    @Test
    public void whenTwoUsersSameFieldsAddedToMapThenTwoKeys() {
        Map<UserHashcode, String> map = new HashMap<>();
        UserHashcode first = new UserHashcode("Ivan", 12, LocalDate.of(1961, 4, 12));
        UserHashcode second = new UserHashcode("Ivan", 12, LocalDate.of(1961, 4, 12));
        assertThat(map.put(first, "first"), is((String) null));
        assertThat(map.put(second, "second"), is((String) null));
    }

}