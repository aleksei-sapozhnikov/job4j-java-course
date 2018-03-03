package ru.job4j.list.stack;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleStackTest {

    /**
     * Test push() and poll()
     */
    @Test
    public void whenPushAndThenPollResultAsExpected() {
        SimpleStack<String> stack = new SimpleStack<>();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        assertThat(stack.poll(), is("3"));
        assertThat(stack.poll(), is("2"));
        assertThat(stack.poll(), is("1"));
        assertThat(stack.poll(), is((String) null));
    }

    @Test
    public void whenPushAndPollInDifferentOrderThatDoesntAffectOnResult() {
        SimpleStack<String> stack = new SimpleStack<>();
        stack.push("1");
        assertThat(stack.poll(), is("1"));
        stack.push("2");
        stack.push("3");
        assertThat(stack.poll(), is("3"));
        stack.push("4");
        assertThat(stack.poll(), is("4"));
        assertThat(stack.poll(), is("2"));
        assertThat(stack.poll(), is((String) null));
        stack.push("5");
        stack.push("6");
        assertThat(stack.poll(), is("6"));
        stack.push("7");
        assertThat(stack.poll(), is("7"));
        assertThat(stack.poll(), is("5"));
        assertThat(stack.poll(), is((String) null));
    }

}