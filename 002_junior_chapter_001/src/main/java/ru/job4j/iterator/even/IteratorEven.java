package ru.job4j.iterator.even;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Array iterator, returning only even numbers.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 27.02.2018
 */
public class IteratorEven implements Iterator<Integer> {

    /**
     * Array to iterate.
     */
    private final int[] values;
    /**
     * Current cursor position.
     */
    private int cursor = 0;

    IteratorEven(final int[] values) {
        this.values = values;
        this.moveCursorToEven();
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return this.cursor < this.values.length;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Integer result = this.values[this.cursor];
        cursor++;
        this.moveCursorToEven();
        return result;
    }

    /**
     * Move cursor to the next even number element.
     */
    private void moveCursorToEven() {
        while (this.cursor < this.values.length && this.values[this.cursor] % 2 != 0) {
            this.cursor++;
        }
    }

}
