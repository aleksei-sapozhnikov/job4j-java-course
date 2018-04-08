package ru.job4j.list.array;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ThreadSafeSimpleArrayListTest {

    /**
     * Test add() and get()
     */
    @Test
    public void whenAddValuesThenListFills() {
        ThreadSafeSimpleArrayList<String> list = new ThreadSafeSimpleArrayList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        assertThat(list.get(0), is("0"));
        assertThat(list.get(1), is("1"));
        assertThat(list.get(2), is("2"));
    }

    /**
     * Test growing of capacity if needed.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void whenCapacityNotEnoughThenGrowCapacity() {
        ThreadSafeSimpleArrayList<Integer> list = new ThreadSafeSimpleArrayList<>(3);
        list.add(0);
        list.add(1);
        list.add(2); //current capacity full
        list.add(3); // new capacity: 3 * 3 / 2 + 1 = 5
        assertThat(list.get(4), is((Integer) null));
        list.get(5);
    }

    /**
     * Test iterator()
     */
    @Test(expected = NoSuchElementException.class)
    public void whenIteratorThenIteratorShowsAllValues() {
        ThreadSafeSimpleArrayList<String> list = new ThreadSafeSimpleArrayList<>(3);
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
        ThreadSafeSimpleArrayList<String> list = new ThreadSafeSimpleArrayList<>(3);
        list.add("0");
        list.add("1");
        Iterator<String> iterator = list.iterator();
        assertThat(iterator.next(), is("0"));
        assertThat(iterator.next(), is("1"));
        list.add("2");
        iterator.hasNext();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenConcurrentAddElementThenNextReturnsConcurrentModificationException() {
        ThreadSafeSimpleArrayList<String> list = new ThreadSafeSimpleArrayList<>(3);
        list.add("0");
        list.add("1");
        Iterator<String> iterator = list.iterator();
        assertThat(iterator.next(), is("0"));
        assertThat(iterator.next(), is("1"));
        list.add("2");
        iterator.next();
    }

    @Test
    public void whenConcurrentGetThenNoConcurrentModificationException() {
        ThreadSafeSimpleArrayList<String> list = new ThreadSafeSimpleArrayList<>(3);
        list.add("0");
        list.add("1");
        list.add("2");
        Iterator<String> iterator = list.iterator();
        iterator.next();
        list.get(1);
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is("1"));
    }
}