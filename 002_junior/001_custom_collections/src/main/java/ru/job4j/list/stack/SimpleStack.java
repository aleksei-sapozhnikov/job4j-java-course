package ru.job4j.list.stack;

import ru.job4j.list.linked.SimpleLinkedList;

import java.util.NoSuchElementException;

/**
 * Simple stack based on SimpleLinkedList.
 * Allows to push elements to the end of the list and
 * poll elements from the end of the list.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.03.2018
 */
public class SimpleStack<E> {

    /**
     * Container for elements.
     */
    private SimpleLinkedList<E> container = new SimpleLinkedList<>();

    /**
     * Add new element to the stack end.
     *
     * @param value element to add.
     */
    public void push(E value) {
        this.container.add(value);
    }

    /**
     * Returns element from the end of the stack and deletes it from the stack.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public E poll() {
        E result;
        try {
            result = this.container.removeLast();
        } catch (NoSuchElementException nse) {
            result = null;
        }
        return result;
    }

}
