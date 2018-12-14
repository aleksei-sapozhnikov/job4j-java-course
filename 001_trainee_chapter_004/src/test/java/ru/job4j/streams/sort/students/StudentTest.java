package ru.job4j.streams.sort.students;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class StudentTest {

    /**
     * Test getters.
     */
    @Test
    public void testGetters() {
        Student student = new Student("vasya", 123);
        assertThat(student.getScore(), is(123));
    }

    /**
     * Test compareTo().
     */
    @Test
    public void testComparableFirstScoreThenName() {
        var students = List.of(
                new Student("angel", 10),
                new Student("angel", 50),
                new Student("zombie", 10)
        );
        var sorted = students.stream().sorted().collect(Collectors.toList());
        assertThat(sorted, is(List.of(
                new Student("angel", 10),
                new Student("zombie", 10),
                new Student("angel", 50)
        )));


    }

    /**
     * Test equals() and hashCode()
     */
    @Test
    public void testEqualsHashcode() {
        Student main = new Student("john", 10);
        Student same = new Student("john", 10);
        Student otherName = new Student("zombie", 10);
        Student otherScore = new Student("john", 50);
        Student otherNull = null;
        String otherClass = "john, 10";
        assertThat(main.equals(otherName), is(false));
        assertThat(main.equals(otherScore), is(false));
        assertThat(main.equals(otherClass), is(false));
        assertThat(main.equals(otherNull), is(false));
        assertThat(main.equals(same), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
    }
}