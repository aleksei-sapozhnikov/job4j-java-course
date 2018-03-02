package ru.job4j.list.linked;

import ru.job4j.list.SimpleContainer;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Allows to store, add and get elements.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 02.03.2018
 */
public class SimpleLinkedList<E> implements SimpleContainer<E> {

    /**
     * Modifications count (to prevent concurrent modification in iterator).
     */
    private int modCount = 0;

    /**
     * First element in the list.
     */
    private Node<E> first = null;

    /**
     * Last element in the list.
     */
    private Node<E> last = null;

    /**
     * Adds new element to the container.
     *
     * @param value element to add.
     */
    @Override
    public void add(E value) {
        this.linkLast(value);
    }

    /**
     * Creates new node and links it as the last in the list.
     *
     * @param value value contained in the new node.
     */
    private void linkLast(E value) {
        final Node<E> oldLast = this.last;
        final Node<E> newNode = new Node<>(oldLast, value, null);
        this.last = newNode;
        if (oldLast == null) {
            this.first = newNode;
        } else {
            oldLast.next = newNode;
        }
        this.modCount++;
    }

    /**
     * Returns element from the container.
     *
     * @param index index of the element to get.
     * @return element if found or {@code null} if not.
     */
    @Override
    public E get(int index) {
        Node<E> node = this.node(index);
        return node != null ? node.item : null;
    }

    /**
     * Returns the (non-null) Node at the specified element index.
     *
     * @return Node if found or {@code null} if not.
     */
    private Node<E> node(int index) {
        Node<E> result = this.first;
        if (result != null && index != 0) {
            for (int i = 0; i < index && result.next != null; i++) {
                result = result.next;
            }
        }
        return result;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            /**
             * Modification count value at the time when the iterator was created.
             * If collection will change during the work of iterator, there will be {@code }ConcurrentModificationException}.
             */
            private final int expectedModCount = modCount;

            /**
             * Next element to iterate.
             */
            private Node<E> cursor = first;

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             * @throws ConcurrentModificationException if the list was changed during the work of iterator.
             */
            @Override
            public boolean hasNext() {
                if (this.expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                return this.cursor != null;
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements.
             * @throws ConcurrentModificationException if the list was changed during the work of iterator.
             */
            @Override
            public E next() {
                if (this.expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                E result = this.cursor.item;
                this.cursor = this.cursor.next;
                return result;
            }
        };
    }

    /**
     * An element of the linked list - node
     * containing item and links to next node and previous node.
     *
     * @param <E> generic parameter this node stores.
     */
    private static class Node<E> {

        /**
         * Item contained.
         */
        private E item;

        /**
         * Link to next Node.
         */
        private Node<E> next;

        /**
         * Link to previous node.
         */
        private Node<E> prev;

        /**
         * @param prev    previous node in the list.
         * @param element item to store in this node.
         * @param next    next node in the list.
         */
        private Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

}
