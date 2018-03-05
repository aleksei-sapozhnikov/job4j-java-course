package ru.job4j.set.array;

import ru.job4j.list.SimpleContainer;
import ru.job4j.list.array.SimpleArrayList;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Allows to store, add and get unique elements.
 * Grows if capacity not enough to add element.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 05.03.2018
 */
public class SimpleArraySet<E> implements Iterable<E> {

    /**
     * Default size for the new set.
     */
    private static final int DEFAULT_SIZE = 10;
    /**
     * Container for elements.
     */
    private SimpleContainer<E> container;
    /**
     * Modifications count (to prevent concurrent modification in iterator).
     */
    private int modCount = 0;

    /**
     * Constructs set with given initial size.
     *
     * @param size initial size of the set.
     */
    public SimpleArraySet(int size) {
        this.container = new SimpleArrayList<>(size);
    }

    /**
     * Constructs set with default initial size (== 10).
     */
    public SimpleArraySet() {
        this(DEFAULT_SIZE);
    }

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param value element to be added to this set.
     * @return {@code true} if this set did not already contain the specified element,
     * {@code false} otherwise.
     */
    public boolean add(E value) {
        boolean adding = !this.contains(value);
        if (adding) {
            this.container.add(value);
            this.modCount++;
        }
        return adding;
    }

    /**
     * Checks if this set contains the specified element.
     *
     * @param value element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element, {@code false} if not.
     */
    public boolean contains(E value) {
        boolean has = false;
        for (E temp : this.container) {
            if (value.equals(temp)) {
                has = true;
                break;
            }
        }
        return has;
    }

    /**
     * Returns an iterator over elements of type {@code E}.
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
             * Iterator of the inner elements container.
             */
            private Iterator<E> itContainer = container.iterator();

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.
             *
             * @return {@code true} if the iteration has more element
             * @throws ConcurrentModificationException if the set was changed during the work of iterator.
             */
            @Override
            public boolean hasNext() {
                if (this.expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                return itContainer.hasNext();
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             * @throws ConcurrentModificationException if the set was changed during the work of iterator.
             */
            @Override
            public E next() {
                if (this.expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return itContainer.next();
            }
        };
    }
}
