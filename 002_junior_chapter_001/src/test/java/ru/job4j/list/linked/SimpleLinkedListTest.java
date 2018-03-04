package ru.job4j.list.linked;

import org.junit.Test;
import ru.job4j.list.linked.SimpleLinkedList.Node;

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
     * Test removeFirst()
     */
    @Test(expected = NoSuchElementException.class)
    public void whenRemoveFirstThenReturnsElementAndOtherElementsChangePosition() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
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
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
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

    /**
     * Test hasCycleFloydAlgorithm() and hasCycleBrentAlgorithm()
     */
    @Test
    public void whenOddNumberOfElementsWithCycleInTheEndThenFindsCycleTrue() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        Node[] nodes = new Node[11]; // imported ru.job4j.list.linked.SimpleLinkedList.Node
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
    public void whenOddNumberOfElementsWithCycleInTheCenterThenFindsCycleTrue() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        Node[] nodes = new Node[11]; // imported ru.job4j.list.linked.SimpleLinkedList.Node
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
    public void whenEvenNumberOfElementsWithCycleInTheEndThenFindsCycleTrue() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        Node[] nodes = new Node[10]; // imported ru.job4j.list.linked.SimpleLinkedList.Node
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
    public void whenEvenNumberOfElementsWithCycleInTheCenterThenFindsCycleTrue() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        Node[] nodes = new Node[10]; // imported ru.job4j.list.linked.SimpleLinkedList.Node
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
    public void whenOddNumberOfElementsWithoutCycleThenFindsCycleFalse() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        Node[] nodes = new Node[11]; // imported ru.job4j.list.linked.SimpleLinkedList.Node
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
    public void whenEvenNumberOfElementsWithoutCycleThenFindsCycleFalse() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        Node[] nodes = new Node[10]; // imported ru.job4j.list.linked.SimpleLinkedList.Node
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