package ru.job4j.list.linked;

import org.junit.Test;
import ru.job4j.list.linked.ThreadSafeSimpleLinkedList.Node;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ThreadSafeSimpleLinkedListTest {

    /**
     * Test multi-threading behaviour: add().
     */
    @Test
    public void whenMultipleThreadsAddValuesAllValuesAreAdded() {
        try {
            ThreadSafeSimpleLinkedList<Integer> list = new ThreadSafeSimpleLinkedList<>();
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
            ThreadSafeSimpleLinkedList<Integer> from = new ThreadSafeSimpleLinkedList<>(); // list to read
            ThreadSafeSimpleLinkedList<Integer> to = new ThreadSafeSimpleLinkedList<>(); // list to write what threads read
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
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        assertThat(list.get(0), is("0"));
        assertThat(list.get(1), is("1"));
        assertThat(list.get(2), is("2"));
        assertThat(list.get(45), is((String) null));
    }

    /**
     * Test removeFirst()
     */
    @Test(expected = NoSuchElementException.class)
    public void whenRemoveFirstThenReturnsElementAndOtherElementsChangePosition() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        assertThat(list.removeFirst(), is("0"));
        assertThat(list.get(0), is("1"));
        assertThat(list.removeFirst(), is("1"));
        assertThat(list.get(0), is("2"));
        assertThat(list.removeFirst(), is("2"));
        assertThat(list.get(0), is((String) null));
        list.removeFirst();
    }

    /**
     * Test removeLast()
     */
    @Test(expected = NoSuchElementException.class)
    public void whenRemoveLastThenReturnsElementAndOtherElementsDoNotChangePosition() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        assertThat(list.get(2), is("2"));
        assertThat(list.removeLast(), is("2"));
        assertThat(list.get(1), is("1"));
        assertThat(list.removeLast(), is("1"));
        assertThat(list.get(0), is("0"));
        assertThat(list.removeLast(), is("0"));
        assertThat(list.get(0), is((String) null));
        list.removeLast();
    }

    /**
     * Test iterator()
     */
    @Test(expected = NoSuchElementException.class)
    public void whenIteratorThenIteratorShowsAllValues() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
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
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        list.add("0");
        list.add("1");
        Iterator<String> iterator = list.iterator();
        iterator.next();
        list.add("2");
        iterator.hasNext();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenConcurrentAddElementThenNextReturnsConcurrentModificationException() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        list.add("0");
        list.add("1");
        Iterator<String> iterator = list.iterator();
        list.add("2");
        iterator.next();
    }

    @Test
    public void whenConcurrentGetThenNoConcurrentModificationException() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
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
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        Iterator<String> iterator = list.iterator();
        assertThat(iterator.hasNext(), is(false));
        iterator.next();
    }

    /**
     * Test hasCycleFloydAlgorithm() and hasCycleBrentAlgorithm()
     */
    @Test
    @SuppressWarnings("unchecked")
    public void whenOddNumberOfElementsWithCycleInTheEndThenFindsCycleTrue() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        Node[] nodes = new Node[11]; // imported ru.job4j.list.linked.ThreadSafeSimpleLinkedList.Node
        nodes[0] = new Node<>(null, 0, null);
        for (int i = 1; i < nodes.length; i++) {
            nodes[i] = new Node<Integer>(nodes[i - 1], i, null);
            nodes[i - 1].next = nodes[i];
        }
        nodes[10].next = nodes[0]; // make cycle
        list.fillWithLinkedNodesArray(nodes);
        assertThat(list.hasCycleFloydAlgorithm(), is(true));
        assertThat(list.hasCycleBrentAlgorithm(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenOddNumberOfElementsWithCycleInTheCenterThenFindsCycleTrue() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        Node[] nodes = new Node[11]; // imported ru.job4j.list.linked.ThreadSafeSimpleLinkedList.Node
        nodes[0] = new Node<>(null, 0, null);
        for (int i = 1; i < nodes.length; i++) {
            nodes[i] = new Node<Integer>(nodes[i - 1], i, null);
            nodes[i - 1].next = nodes[i];
        }
        nodes[7].next = nodes[3]; // make cycle
        list.fillWithLinkedNodesArray(nodes);
        assertThat(list.hasCycleFloydAlgorithm(), is(true));
        assertThat(list.hasCycleBrentAlgorithm(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenEvenNumberOfElementsWithCycleInTheEndThenFindsCycleTrue() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        Node[] nodes = new Node[10]; // imported ru.job4j.list.linked.ThreadSafeSimpleLinkedList.Node
        nodes[0] = new Node<>(null, 0, null);
        for (int i = 1; i < nodes.length; i++) {
            nodes[i] = new Node<Integer>(nodes[i - 1], i, null);
            nodes[i - 1].next = nodes[i];
        }
        nodes[9].next = nodes[0]; // make cycle
        list.fillWithLinkedNodesArray(nodes);
        assertThat(list.hasCycleFloydAlgorithm(), is(true));
        assertThat(list.hasCycleBrentAlgorithm(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenEvenNumberOfElementsWithCycleInTheCenterThenFindsCycleTrue() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        Node[] nodes = new Node[10]; // imported ru.job4j.list.linked.ThreadSafeSimpleLinkedList.Node
        nodes[0] = new Node<>(null, 0, null);
        for (int i = 1; i < nodes.length; i++) {
            nodes[i] = new Node<Integer>(nodes[i - 1], i, null);
            nodes[i - 1].next = nodes[i];
        }
        nodes[7].next = nodes[3]; // make cycle
        list.fillWithLinkedNodesArray(nodes);
        assertThat(list.hasCycleFloydAlgorithm(), is(true));
        assertThat(list.hasCycleBrentAlgorithm(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenOddNumberOfElementsWithoutCycleThenFindsCycleFalse() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        Node[] nodes = new Node[11]; // imported ru.job4j.list.linked.ThreadSafeSimpleLinkedList.Node
        nodes[0] = new Node<>(null, 0, null);
        for (int i = 1; i < nodes.length; i++) {
            nodes[i] = new Node<Integer>(nodes[i - 1], i, null);
            nodes[i - 1].next = nodes[i];
        }
        list.fillWithLinkedNodesArray(nodes);
        assertThat(list.hasCycleFloydAlgorithm(), is(false));
        assertThat(list.hasCycleBrentAlgorithm(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenEvenNumberOfElementsWithoutCycleThenFindsCycleFalse() {
        ThreadSafeSimpleLinkedList<String> list = new ThreadSafeSimpleLinkedList<>();
        Node[] nodes = new Node[10]; // imported ru.job4j.list.linked.ThreadSafeSimpleLinkedList.Node
        nodes[0] = new Node<>(null, 0, null);
        for (int i = 1; i < nodes.length; i++) {
            nodes[i] = new Node<Integer>(nodes[i - 1], i, null);
            nodes[i - 1].next = nodes[i];
        }
        list.fillWithLinkedNodesArray(nodes);
        assertThat(list.hasCycleFloydAlgorithm(), is(false));
        assertThat(list.hasCycleBrentAlgorithm(), is(false));
    }
}