package ru.job4j.list.queue;

import ru.job4j.list.linked.SimpleLinkedList;

import java.util.NoSuchElementException;

/**
 * Simple queue based on SimpleLinkedList.
 * Allows to push elements to the end of the list and
 * poll elements from the head of the list.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.03.2018
 */
public class SimpleQueue<E> {

    /**
     * Container for elements.
     */
    private SimpleLinkedList<E> container = new SimpleLinkedList<>();

    /**
     * Add new element to the queue tail.
     *
     * @param value element to add.
     */
    public void push(E value) {
        this.container.add(value);
    }

    /**
     * Returns first element from the head and deletes it from the queue.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public E poll() {
        E result;
        try {
            result = this.container.removeFirst();
        } catch (NoSuchElementException nse) {
            result = null;
        }
        return result;
    }

}
