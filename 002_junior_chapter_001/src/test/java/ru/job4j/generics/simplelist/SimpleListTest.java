package ru.job4j.generics.simplelist;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for SimpleList class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 01.03.2018
 */
public class SimpleListTest {

    /**
     * Test add() and get()
     */
    @Test
    public void whenAddValuesThenListFills() {
        SimpleList<Integer> list = new SimpleList<>(3);
        list.add(1);
        list.add(2);
        list.add(3);
        assertThat(list.get(0), is(1));
        assertThat(list.get(1), is(2));
        assertThat(list.get(2), is(3));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteFromBeginningThenWorksCorrect() {
        SimpleList<String> list = new SimpleList<>(5);
        list.add("a");
        list.add("b");
        list.add("c");
        list.delete(0);
        assertThat(list.get(0), is("b"));
        assertThat(list.get(1), is("c"));
        assertThat(list.get(2), is((String) null));
    }

    @Test
    public void whenDeleteFromCenterThenWorksCorrect() {
        SimpleList<String> list = new SimpleList<>(5);
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.delete(2);
        assertThat(list.get(0), is("a"));
        assertThat(list.get(1), is("b"));
        assertThat(list.get(2), is("d"));
        assertThat(list.get(3), is("e"));
        assertThat(list.get(4), is((String) null));
    }

    @Test
    public void whenDeleteLastThenLastIsNull() {
        SimpleList<String> list = new SimpleList<>(3);
        list.add("a");
        list.add("b");
        list.add("c");
        list.delete(2);
        assertThat(list.get(0), is("a"));
        assertThat(list.get(1), is("b"));
        assertThat(list.get(2), is((String) null));
    }

    /**
     * Test set()
     */
    @Test
    public void whenSetElementsThenNewValue() {
        SimpleList<String> list = new SimpleList<>(3);
        list.add("0");
        list.add("1");
        list.add("2");
        list.set(0, "10");
        list.set(2, "12");
        assertThat(list.get(0), is("10"));
        assertThat(list.get(1), is("1"));
        assertThat(list.get(2), is("12"));

    }

    /**
     * Test iterator()
     */
    @Test(expected = NoSuchElementException.class)
    public void whenIteratorThenIteratorShowsAllValues() {
        SimpleList<String> list = new SimpleList<>(3);
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
        assertThat(iterator.next(), is("2"));
        assertThat(iterator.hasNext(), is(false));
        iterator.next();
    }

    /**
     * Test growing of capacity if needed.
     */
    @Test
    public void whenCapacityNotEnoughThenGrowCapacity() {
        SimpleList<String> list = new SimpleList<>(3);
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4"); //new capacity: 3 * 3 / 2 + 1 = 5
        list.add("5");
    }
}