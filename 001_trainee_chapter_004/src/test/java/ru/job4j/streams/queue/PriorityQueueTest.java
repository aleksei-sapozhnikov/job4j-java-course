package ru.job4j.streams.queue;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the PriorityQueue class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.02.2018
 */
public class PriorityQueueTest {

    /**
     * Test put() method.
     */
    @Test
    public void whenDifferentPrioritiesThenOneWithTheHighestFirst() {
        PriorityQueue queue = new PriorityQueue();
        queue.put(new Task("low", 5));
        queue.put(new Task("urgent", 1));
        queue.put(new Task("middle", 3));
        Task result = queue.poll();
        assertThat(result.description(), is("urgent"));
    }

    @Test
    public void whenDifferentPrioritiesThenHighestInOrderAsAdded() {
        PriorityQueue queue = new PriorityQueue();
        queue.put(new Task("urgentOne", 1));
        queue.put(new Task("low", 5));
        queue.put(new Task("urgentTwo", 1));
        queue.put(new Task("middleOne", 3));
        Task resultOne = queue.poll();
        Task resultTwo = queue.poll();
        Task resultThree = queue.poll();
        assertThat(resultOne.description(), is("urgentOne"));
        assertThat(resultTwo.description(), is("urgentTwo"));
        assertThat(resultThree.description(), is("middleOne"));
    }

    /**
     * Test putByIterator() method.
     */
    @Test
    public void whenDifferentPrioritiesThenOneWithTheHighestFirstByIterator() {
        PriorityQueue queue = new PriorityQueue();
        queue.putByIterator(new Task("low", 5));
        queue.putByIterator(new Task("urgent", 1));
        queue.putByIterator(new Task("middle", 3));
        Task result = queue.poll();
        assertThat(result.description(), is("urgent"));
    }

    @Test
    public void whenDifferentPrioritiesThenHighestInOrderAsAddedByIterator() {
        PriorityQueue queue = new PriorityQueue();
        queue.putByIterator(new Task("urgentOne", 1));
        queue.putByIterator(new Task("low", 5));
        queue.putByIterator(new Task("urgentTwo", 1));
        queue.putByIterator(new Task("middleOne", 3));
        Task resultOne = queue.poll();
        Task resultTwo = queue.poll();
        Task resultThree = queue.poll();
        assertThat(resultOne.description(), is("urgentOne"));
        assertThat(resultTwo.description(), is("urgentTwo"));
        assertThat(resultThree.description(), is("middleOne"));
    }

    /**
     * Test poll() method.
     */
    @Test
    public void whenPollTaskThenTask() {
        PriorityQueue queue = new PriorityQueue();
        queue.put(new Task("urgentOne", 1));
        Task task = queue.poll();
        String resultOne = task.description();
        int resultTwo = task.priority();
        assertThat(resultOne, is("urgentOne"));
        assertThat(resultTwo, is(1));
    }
}