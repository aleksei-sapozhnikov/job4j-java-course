package ru.job4j.list.linked;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SimpleLinkedListTest {

    /**
     * Test add() and get()
     */
    @Test
    public void whenAddValuesThenListFills() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        assertThat(list.get(0), is("0"));
        assertThat(list.get(1), is("1"));
        assertThat(list.get(2), is("2"));
    }

    /**
     * Test iterator()
     */
    @Test(expected = NoSuchElementException.class)
    public void whenIteratorThenIteratorShowsAllValues() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        Iterator<String> iterator = list.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is("0"));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is("1"));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is("2"));
        assertThat(iterator.hasNext(), is(false));
        iterator.next();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenConcurrentAddElementThenHasNextReturnsConcurrentModificationException() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        list.add("0");
        list.add("1");
        Iterator<String> iterator = list.iterator();
        iterator.next();
        list.add("2");
        iterator.hasNext();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenConcurrentAddElementThenNextReturnsConcurrentModificationException() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        list.add("0");
        list.add("1");
        Iterator<String> iterator = list.iterator();
        list.add("2");
        iterator.next();
    }

    @Test
    public void whenConcurrentGetThenNoConcurrentModificationException() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        Iterator<String> iterator = list.iterator();
        iterator.next();
        list.get(1);
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is("1"));
    }

    @Test(expected = NoSuchElementException.class)
    public void whenIteratorOnEmptyListThenHasNextFalseAndNextNoSuchElementException() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        Iterator<String> iterator = list.iterator();
        assertThat(iterator.hasNext(), is(false));
        iterator.next();
    }

}