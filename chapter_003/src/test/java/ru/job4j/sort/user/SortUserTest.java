package ru.job4j.sort.user;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the SortUser class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 13.02.2018
 */
public class SortUserTest {

    /**
     * Test sort() method.
     */
    @Test
    public void whenListOfUsersThenSetSortedByAge() {
        List<User> list = new ArrayList<>(Arrays.asList(
                new User("Jan", 78),
                new User("Smith", 22),
                new User("Colin", 32),
                new User("Zebra", 2),
                new User("Garry", 120)
        ));
        User[] result = new SortUser().sort(list).toArray(new User[list.size()]);
        User[] expected = new User[]{
                new User("Zebra", 2),
                new User("Smith", 22),
                new User("Colin", 32),
                new User("Jan", 78),
                new User("Garry", 120)
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenEmptyListOfUsersThenEmptySet() {
        List<User> list = new ArrayList<>();
        User[] result = new SortUser().sort(list).toArray(new User[list.size()]);
        User[] expected = new User[]{};
        assertThat(result, is(expected));
    }

    /**
     * Test sortNameLength() method
     */
    @Test
    public void whenDifferentNameLengthsThenSortedByLength() {
        List<User> input = new ArrayList<>(Arrays.asList(
                new User("Ivan", 5),
                new User("Kuharka", 3),
                new User("Ya", 23)
        ));
        List<User> result = new SortUser().sortNameLength(input);
        List<User> expected = new ArrayList<>(Arrays.asList(
                new User("Ya", 23),
                new User("Ivan", 5),
                new User("Kuharka", 3)
        ));
        assertThat(result, is(expected));
    }

    /**
     * Test sortByAllFields() method
     */
    @Test
    public void whenEqualNamesThenSortedByAge() {
        List<User> input = new ArrayList<>(Arrays.asList(
                new User("John", 87),
                new User("John", 2),
                new User("John", 54),
                new User("John", 23)

        ));
        List<User> result = new SortUser().sortByAllFields(input);
        List<User> expected = new ArrayList<>(Arrays.asList(
                new User("John", 2),
                new User("John", 23),
                new User("John", 54),
                new User("John", 87)
        ));
        assertThat(result, is(expected));
    }

    @Test
    public void whenEqualAgesThenSortedByName() {
        List<User> input = new ArrayList<>(Arrays.asList(
                new User("John", 87),
                new User("Yan", 87),
                new User("Anna", 87)
        ));
        List<User> result = new SortUser().sortByAllFields(input);
        List<User> expected = new ArrayList<>(Arrays.asList(
                new User("Anna", 87),
                new User("John", 87),
                new User("Yan", 87)
        ));
        assertThat(result, is(expected));
    }

    @Test
    public void whenDifferentNameAndAgeThenSortedByNameThenByAge() {
        List<User> input = new ArrayList<>(Arrays.asList(
                new User("John", 87),
                new User("Yan", 87),
                new User("Anna", 87),
                new User("John", 23),
                new User("Anna", 12)
        ));
        List<User> result = new SortUser().sortByAllFields(input);
        List<User> expected = new ArrayList<>(Arrays.asList(
                new User("Anna", 12),
                new User("Anna", 87),
                new User("John", 23),
                new User("John", 87),
                new User("Yan", 87)
        ));
        assertThat(result, is(expected));
    }

    @Test
    public void whenDifferentNameAndAgeThenSortedByNameThenByAge2() {
        List<User> input = new ArrayList<>(Arrays.asList(
                new User("Сергей", 25),
                new User("Иван", 30),
                new User("Сергей", 20),
                new User("Иван", 25)
        ));
        List<User> result = new SortUser().sortByAllFields(input);
        List<User> expected = new ArrayList<>(Arrays.asList(
                new User("Иван", 25),
                new User("Иван", 30),
                new User("Сергей", 20),
                new User("Сергей", 25)
        ));
        assertThat(result, is(expected));
    }

}