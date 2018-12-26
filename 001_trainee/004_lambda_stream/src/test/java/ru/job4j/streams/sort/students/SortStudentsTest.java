package ru.job4j.streams.sort.students;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SortStudentsTest {

    private SortStudents sorter = new SortStudents();

    /**
     * Test levelOf().
     */
    @Test
    public void whenScoreMoreOrEqualBoundThenPass() {
        List<Student> students = Arrays.asList(
                new Student("vasya", 124),
                new Student("masha", 12),
                null,
                new Student("roma", 43),
                null,
                new Student("sasha", 66)
        );
        assertThat(sorter.levelOf(students, 66), is(List.of(
                new Student("vasya", 124),
                new Student("sasha", 66)
        )));
    }
}