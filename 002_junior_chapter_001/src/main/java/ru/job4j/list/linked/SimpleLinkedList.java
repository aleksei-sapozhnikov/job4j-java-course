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
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list.
     * @throws NoSuchElementException if this list is empty (first element == null).
     */
    public E removeFirst() {
        if (this.first == null) {
            throw new NoSuchElementException();
        }
        return unlinkFirst();
    }

    /**
     * Unlinks non-null first node.
     *
     * @return element contained in the first node
     */
    private E unlinkFirst() {
        final E element = this.first.item;
        this.first = this.first.next;
        if (this.first == null) {
            this.last = null;
        } else {
            this.first.prev = null;
        }
        this.modCount++;
        return element;
    }

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list.
     * @throws NoSuchElementException if this list is empty (first element == null).
     */
    public E removeLast() {
        if (this.last == null) {
            throw new NoSuchElementException();
        }
        return unlinkLast();
    }

    /**
     * Unlinks non-null first node.
     *
     * @return element contained in the first node
     */
    private E unlinkLast() {
        final E element = this.last.item;
        this.last = this.last.prev;
        if (this.last == null) {
            this.first = null;
        } else {
            this.last.next = null;
        }
        this.modCount++;
        return element;
    }

    /**
     * Finding cycle using Floyd's cycle-finding algorithm ('The hare and the tortoise').
     * http://www.siafoo.net/algorithm/10
     * <p>
     * There are two pointers travelling over the list node-by-node: the hare (fast) and the tortoise (slow).
     * Every iteration the hare makes two steps, and the turtle makes one step forward.
     * If there are no loops in the list, hare will come to the end first (his next element will be null).
     * If there is a loop, the hare will finally run behind the tortoise and then get it (they will point to the same node).
     *
     * @return {@code true} if found cycle, {@code false} if not.
     */
    public boolean hasCycleFloydAlgorithm() {
        Node<E> fast = this.first;
        Node<E> slow = this.first;
        boolean cycle = false;
        while (!cycle && fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) { // yes, the same object!
                cycle = true;
            }
        }
        return cycle;
    }

    /**
     * Finding cycle using Richard Brent's cycle-finding algorithm.
     * This is an improved version of Floyd's algorithm, called 'teleporting turtle".
     * http://www.siafoo.net/algorithm/11.
     * <p>
     * The main change is that turtle doesn't move constantly to one step forward and the hare makes only one step at a time.
     * The turtle stays in the same place until the hare makes some maximum amount of steps. After that the turtle
     * 'teleports' and waits the rabbit again. The amount of hare's maximum steps increases at each 'teleportation' as
     * there are more and more elements behind the turtle, and she needs to wait for possible rabbit's coming.
     *
     * @return {@code true} if found cycle, {@code false} if not.
     */
    public boolean hasCycleBrentAlgorithm() {
        Node<E> fast = this.first;
        Node<E> slow = this.first;
        int taken = 0;
        int max = 2;
        boolean cycle = false;
        while (!cycle && fast.next != null) {
            fast = fast.next;
            taken++;
            cycle = slow == fast;
            if (!cycle && taken == max) {
                taken = 0;
                max *= 2;
                slow = fast;
            }
        }
        return cycle;
    }

    /**
     * Method is just for for tests, fills list with array of nodes.
     * Assuming all nodes are already connected to each other.
     * First node in the array will be the first, last - the last.
     *
     * @param nodes array of nodes, linked to each other.
     */
    void fillWithLinkedNodesArray(Node<E>[] nodes) {
        this.first = nodes[0];
        this.last = nodes[nodes.length - 1];
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
    static class Node<E> {

        /**
         * Item contained.
         */
        private E item;

        /**
         * Link to next Node.
         */
        Node<E> next;

        /**
         * Link to previous node.
         */
        Node<E> prev;

        /**
         * @param prev    previous node in the list.
         * @param element item to store in this node.
         * @param next    next node in the list.
         */
        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

}
