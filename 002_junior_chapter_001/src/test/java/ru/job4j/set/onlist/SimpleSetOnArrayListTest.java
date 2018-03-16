package ru.job4j.set.onlist;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for the SimpleArraySet class for the set based on array list.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 05.03.2018
 */
public class SimpleSetOnArrayListTest {

    /**
     * Test add()
     */
    @Test
    public void whenAddElementsThenFalseIfAlreadyContainedThemAndTrueIfDidnt() {
        SimpleSetOnArrayList<String> set = new SimpleSetOnArrayList<>();
        assertThat(set.add("0"), is(true));
        assertThat(set.add("1"), is(true));
        assertThat(set.add("1"), is(false));
        assertThat(set.add("2"), is(true));
        assertThat(set.add("0"), is(false));

    }

    @Test
    public void whenSizeNotEnoughThenGrows() {
        SimpleSetOnArrayList<String> set = new SimpleSetOnArrayList<>(2);
        assertThat(set.add("0"), is(true));
        assertThat(set.add("1"), is(true)); // full
        assertThat(set.add("2"), is(true)); // grew
        assertThat(set.add("3"), is(true));
        assertThat(set.add("4"), is(true));
        assertThat(set.add("5"), is(true));
    }

    /**
     * Test contains()
     */
    @Test
    public void whenContainsElementThenTrueAndFalseOtherwise() {
        SimpleSetOnArrayList<String> set = new SimpleSetOnArrayList<>();
        set.add("0");
        set.add("2");
        set.add("3");
        assertThat(set.contains("0"), is(true));
        assertThat(set.contains("1"), is(false));
        assertThat(set.contains("2"), is(true));
        assertThat(set.contains("3"), is(true));
        assertThat(set.contains("4"), is(false));
    }

    /**
     * Test iterator()
     */
    @Test(expected = NoSuchElementException.class)
    public void whenIteratorThenIteratorShowsAllValues() {
        SimpleSetOnArrayList<String> set = new SimpleSetOnArrayList<>(3);
        set.add("0");
        set.add("1");
        set.add("2");
        Iterator<String> iterator = set.iterator();
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
        SimpleSetOnArrayList<String> set = new SimpleSetOnArrayList<>(3);
        set.add("0");
        set.add("1");
        Iterator<String> iterator = set.iterator();
        assertThat(iterator.next(), is("0"));
        assertThat(iterator.next(), is("1"));
        set.add("2");
        iterator.hasNext();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenConcurrentAddElementThenNextReturnsConcurrentModificationException() {
        SimpleSetOnArrayList<String> set = new SimpleSetOnArrayList<>(3);
        set.add("0");
        set.add("1");
        Iterator<String> iterator = set.iterator();
        assertThat(iterator.next(), is("0"));
        assertThat(iterator.next(), is("1"));
        set.add("2");
        iterator.next();
    }

    @Test
    public void whenConcurrentGetThenNoConcurrentModificationException() {
        SimpleSetOnArrayList<String> set = new SimpleSetOnArrayList<>(3);
        set.add("0");
        set.add("1");
        set.add("2");
        Iterator<String> iterator = set.iterator();
        iterator.next();
        assertThat(set.contains("1"), is(true));
        assertThat(set.contains("2"), is(true));
        assertThat(set.contains("4"), is(false));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is("1"));
    }
}