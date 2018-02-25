package ru.job4j.queue;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TaskTest {

    /**
     * Test description() method.
     */
    @Test
    public void whenDescriptionThenDescriptionString() {
        Task task = new Task("desc", 4);
        String result = task.description();
        assertThat(result, is("desc"));
    }

    /**
     * Test priority() method.
     */
    @Test
    public void whenPriorityThenPriorityString() {
        Task task = new Task("desc", 4);
        int result = task.priority();
        assertThat(result, is(4));
    }
}