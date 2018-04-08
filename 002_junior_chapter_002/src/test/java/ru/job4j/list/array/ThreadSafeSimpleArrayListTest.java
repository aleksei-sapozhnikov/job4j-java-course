package ru.job4j.list.array;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ThreadSafeSimpleArrayListTest {

    /**
     * Test multi-threading behaviour: add().
     */
    @Test
    public void whenMultipleThreadsAddValuesAllValuesAreAdded() {
        try {
            ThreadSafeSimpleArrayList<Integer> list = new ThreadSafeSimpleArrayList<>();
            // fill list
            final int length = 50;
            Thread[] threads = new Thread[length];
            for (int i = 0; i < length; i++) {
                final int finalI = i;
                threads[i] = new Thread(() -> list.add(finalI));
            }
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
            // sort what we got
            ArrayList<Integer> sorted = new ArrayList<>();
            for (Integer a : list) {
                sorted.add(a);
            }
            sorted.sort(Comparator.naturalOrder());
            // what we expect
            Integer[] expected = new Integer[50];
            for (int i = 0; i < length; i++) {
                expected[i] = i;
            }
            // check result
            Integer[] result = sorted.toArray(new Integer[0]);
            assertThat(result, is(expected));
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Test multi-threading behaviour: iterator().
     */
    @Test
    public void whenMultipleThreadsReadIteratorThenAllValuesAreRead() {
        try {
            ThreadSafeSimpleArrayList<Integer> from = new ThreadSafeSimpleArrayList<>(); // list to read
            ThreadSafeSimpleArrayList<Integer> to = new ThreadSafeSimpleArrayList<>();// list to write what threads read
            // fill "from" and get iterator
            final int length = 50;
            for (int i = 0; i < length; i++) {
                from.add(i);
            }
            Iterator<Integer> iterator = from.iterator();
            // threads reading "from" and writing "to"
            Thread[] threads = new Thread[length];
            for (int i = 0; i < length; i++) {
                threads[i] = new Thread(() -> to.add(iterator.next()));
            }
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
            // sort what we got in list "to"
            ArrayList<Integer> sorted = new ArrayList<>();
            for (Integer temp : to) {
                sorted.add(temp);
            }
            sorted.sort(Comparator.naturalOrder());
            // what we expect
            Integer[] expected = new Integer[50];
            for (int i = 0; i < length; i++) {
                expected[i] = i;
            }
            // check result
            Integer[] result = sorted.toArray(new Integer[0]);
            assertThat(result, is(expected));
            assertThat(iterator.hasNext(), is(false));
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

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