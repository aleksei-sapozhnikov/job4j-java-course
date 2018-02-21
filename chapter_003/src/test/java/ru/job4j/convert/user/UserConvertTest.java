package ru.job4j.convert.user;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for UserConvert class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 13.02.2018
 */
public class UserConvertTest {

    /**
     * Test process() method.
     */
    @Test
    public void whenListOfSingleUserThenMapOfSingleUser() {
        List<User> input = new ArrayList<>();
        input.add(new User(12, "Inna", "Moscow"));
        Map<Integer, User> result = new UserConvert().process(input);
        Map<Integer, User> expected = new HashMap<>();
        expected.put(12, new User(12, "Inna", "Moscow"));
        assertThat(result, is(expected));
    }

    @Test
    public void whenEmptyListOfUsersThenEmptyMap() {
        List<User> input = new ArrayList<>();
        Map<Integer, User> result = new UserConvert().process(input);
        Map<Integer, User> expected = new HashMap<>();
        assertThat(result, is(expected));
    }

    @Test
    public void whenListOfUsersThenMapOfUsers() {
        List<User> input = new ArrayList<>();
        input.add(new User(12, "Inna", "Moscow"));
        input.add(new User(34, "Anna", "Petersburg"));
        input.add(new User(32, "Inna", "London"));
        Map<Integer, User> result = new UserConvert().process(input);
        Map<Integer, User> expected = new HashMap<>();
        expected.put(12, new User(12, "Inna", "Moscow"));
        expected.put(34, new User(34, "Anna", "Petersburg"));
        expected.put(32, new User(32, "Inna", "London"));
        assertThat(result, is(expected));
    }
}