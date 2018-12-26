package ru.job4j.list.queue;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleQueueTest {

    /**
     * Test push() and poll()
     */
    @Test
    public void whenPushAndThenPollResultAsExpected() {
        SimpleQueue<String> queue = new SimpleQueue<>();
        queue.push("1");
        queue.push("2");
        queue.push("3");
        assertThat(queue.poll(), is("1"));
        assertThat(queue.poll(), is("2"));
        assertThat(queue.poll(), is("3"));
        assertThat(queue.poll(), is((String) null));
    }

    @Test
    public void whenPushAndPollInDifferentOrderThatDoesntAffectOnResult() {
        SimpleQueue<String> queue = new SimpleQueue<>();
        queue.push("1");
        assertThat(queue.poll(), is("1"));
        queue.push("2");
        queue.push("3");
        assertThat(queue.poll(), is("2"));
        queue.push("4");
        assertThat(queue.poll(), is("3"));
        assertThat(queue.poll(), is("4"));
        assertThat(queue.poll(), is((String) null));
        queue.push("5");
        queue.push("6");
        assertThat(queue.poll(), is("5"));
        queue.push("7");
        assertThat(queue.poll(), is("6"));
        assertThat(queue.poll(), is("7"));
        assertThat(queue.poll(), is((String) null));
    }

}