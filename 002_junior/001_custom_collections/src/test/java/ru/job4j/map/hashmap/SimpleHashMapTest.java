package ru.job4j.map.hashmap;

import javafx.util.Pair;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleHashMapTest {

    /**
     * Test insert()
     */
    @Test
    public void whenInsertElementToEmptyBucketThenTrueAndTheyAreInTheRightPlace() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(5);
        assertThat(map.insert(0, "zero"), is(true));
        assertThat(map.insert(2, "two"), is(true));
        assertThat(map.insert(3, "three"), is(true));
        String result = map.toString();
        String expected = new StringJoiner(System.lineSeparator())
                .add("bucket #0: 0 = zero")
                .add("bucket #1: null = null")
                .add("bucket #2: 2 = two")
                .add("bucket #3: 3 = three")
                .add("bucket #4: null = null")
                .toString();
        assertThat(result, is(expected));
    }

    @Test
    public void whenInsertElementWithDifferentKeyToOccupiedBucketThenFalseAndBucketNotChanged() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(3);
        assertThat(map.insert(0, "zero"), is(true));
        assertThat(map.insert(3, "three"), is(false));
        assertThat(map.insert(6, "six"), is(false));
        String result = map.toString();
        String expected = new StringJoiner(System.lineSeparator())
                .add("bucket #0: 0 = zero")
                .add("bucket #1: null = null")
                .add("bucket #2: null = null")
                .toString();
        assertThat(result, is(expected));
    }

    @Test
    public void whenInsertElementWithTheSameKeyToOccupiedBucketThenTrueAndValueChanged() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(3);
        assertThat(map.insert(0, "zero"), is(true));
        assertThat(map.insert(0, "one"), is(true));
        String result = map.toString();
        String expected = new StringJoiner(System.lineSeparator())
                .add("bucket #0: 0 = one")
                .add("bucket #1: null = null")
                .add("bucket #2: null = null")
                .toString();
        assertThat(result, is(expected));
    }

    /**
     * Test get()
     */
    @Test
    public void whenGetContainingKeyThenReturnValue() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(5);
        map.insert(0, "zero");
        map.insert(2, "two");
        assertThat(map.get(0), is("zero"));
        assertThat(map.get(2), is("two"));
    }

    @Test
    public void whenGetNotContainingKeyThenReturnNull() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(5);
        map.insert(0, "zero");
        map.insert(2, "two");
        assertThat(map.get(1), is((String) null));
        assertThat(map.get(5), is((String) null)); // same bucket as 0
        assertThat(map.get(21), is((String) null));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteContainedThenReturnTrueAndBucketBecomesNull() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(3);
        map.insert(0, "zero");
        map.insert(2, "two");
        assertThat(map.delete(2), is(true));
        String result = map.toString();
        String expected = new StringJoiner(System.lineSeparator())
                .add("bucket #0: 0 = zero")
                .add("bucket #1: null = null")
                .add("bucket #2: null = null")
                .toString();
        assertThat(result, is(expected));
    }

    @Test
    public void whenDeleteNotContainedThenReturnFalseAndBucketNotChanged() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(3);
        map.insert(0, "zero");
        map.insert(2, "two");
        assertThat(map.delete(1), is(false));
        assertThat(map.delete(3), is(false)); // same bucket as for 0
        String result = map.toString();
        String expected = new StringJoiner(System.lineSeparator())
                .add("bucket #0: 0 = zero")
                .add("bucket #1: null = null")
                .add("bucket #2: 2 = two")
                .toString();
        assertThat(result, is(expected));
    }

    @Test(expected = NoSuchElementException.class)
    public void whenIterateThenHasNextAndNextWorkProperly() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(5);
        map.insert(0, "zero");
        map.insert(2, "two");
        map.insert(4, "four");
        Iterator<Pair<Integer, String>> iterator = map.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(new Pair<>(0, "zero")));
        assertThat(iterator.next(), is(new Pair<>(2, "two")));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(new Pair<>(4, "four")));
        assertThat(iterator.hasNext(), is(false));
        assertThat(iterator.hasNext(), is(false));
        assertThat(iterator.hasNext(), is(false));
        iterator.next();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenConcurrentInsertThenHasNextThrowsConcurrentModificationException() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(5);
        map.insert(0, "zero");
        map.insert(2, "two");
        map.insert(4, "four");
        Iterator<Pair<Integer, String>> iterator = map.iterator();
        assertThat(iterator.next(), is(new Pair<>(0, "zero")));
        map.get(0);
        assertThat(iterator.next(), is(new Pair<>(2, "two")));
        map.insert(0, "null");
        iterator.hasNext();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenConcurrentInsertThenNextThrowsConcurrentModificationException() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(6);
        map.insert(0, "zero");
        map.insert(2, "two");
        map.insert(4, "four");
        Iterator<Pair<Integer, String>> iterator = map.iterator();
        assertThat(iterator.next(), is(new Pair<>(0, "zero")));
        map.get(0);
        assertThat(iterator.next(), is(new Pair<>(2, "two")));
        map.insert(0, "null");
        iterator.next();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenConcurrentRemoveThenHasNextThrowsConcurrentModificationException() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(5);
        map.insert(0, "zero");
        map.insert(2, "two");
        map.insert(4, "four");
        Iterator<Pair<Integer, String>> iterator = map.iterator();
        assertThat(iterator.next(), is(new Pair<>(0, "zero")));
        map.get(0);
        assertThat(iterator.next(), is(new Pair<>(2, "two")));
        map.delete(0);
        iterator.hasNext();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenConcurrentRemoveThenNextThrowsConcurrentModificationException() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(5);
        map.insert(0, "zero");
        map.insert(2, "two");
        map.insert(4, "four");
        Iterator<Pair<Integer, String>> iterator = map.iterator();
        assertThat(iterator.next(), is(new Pair<>(0, "zero")));
        map.get(0);
        assertThat(iterator.next(), is(new Pair<>(2, "two")));
        map.delete(0);
        iterator.next();
    }

    /**
     * Test growIfNeeded()
     */
    @Test
    public void whenSizeIsNotEnoughThenGrows() {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(5);
        map.insert(0, "zero");
        map.insert(2, "two");
        map.insert(6, "six"); // bucket #1
        map.insert(4, "four"); // (4 / 5 == 80%) filled => new size : (5 * 3 / 2 + 1 == 8)
        String result = map.toString();
        String expected = new StringJoiner(System.lineSeparator())
                .add("bucket #0: 0 = zero")
                .add("bucket #1: null = null") // now bucket #1 is empty
                .add("bucket #2: 2 = two")
                .add("bucket #3: null = null")
                .add("bucket #4: 4 = four")
                .add("bucket #5: null = null")
                .add("bucket #6: 6 = six") // 6 is here
                .add("bucket #7: null = null")
                .toString();
        assertThat(result, is(expected));
    }

}