package ru.job4j.generics.simplelist;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple array-based list with fixed size.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 01.03.2018
 */
public class SimpleList<T> implements Iterable<T> {

    /**
     * Stored objects.
     */
    private final T[] values;

    /**
     * Position where to add next object.
     */
    private int position = 0;

    /**
     * @param size size of the list.
     */
    @SuppressWarnings("unchecked")
    SimpleList(int size) {
        this.values = (T[]) new Object[size];
    }

    /**
     * Adds new value to the end of the list.
     *
     * @param value value to add.
     */
    void add(T value) {
        this.values[position++] = value;
    }

    /**
     * Replaces element with a new value.
     *
     * @param index index of the element to replace.
     * @param value new value.
     */
    void set(int index, T value) {
        this.values[index] = value;
    }

    /**
     * Deletes element.
     *
     * @param index position of the element to delete.
     */
    void delete(int index) {
        System.arraycopy(this.values, index + 1, this.values, index, --this.position - index);
        this.values[this.position] = null;
    }

    /**
     * Get element in the position.
     *
     * @param index position of the element.
     * @return element in this position.
     */
    T get(int index) {
        return this.values[index];
    }

    /**
     * Returns iterator to traverse through this list.
     *
     * @return iterator to traverse through this list.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            /**
             * Position of the iterator.
             */
            private int cursor = 0;

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                return this.cursor < position;
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return values[cursor++];
            }
        };
    }


}
