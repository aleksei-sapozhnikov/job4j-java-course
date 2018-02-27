package ru.job4j.iterator.prime;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Array iterator, returning only prime numbers.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 27.02.2018
 */
public class IteratorPrime implements Iterator<Integer> {

    /**
     * Current cursor position.
     */
    private int cursor = 0;

    /**
     * Array to iterate.
     */
    private final int[] values;

    IteratorPrime(final int[] values) {
        this.values = values;
        this.moveCursorToPrime();
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
        this.moveCursorToPrime();
        return result;
    }

    /**
     * Move cursor to the next even number element.
     */
    private void moveCursorToPrime() {
        while (this.cursor < this.values.length
                && !this.isPrime(this.values[this.cursor])) {
            this.cursor++;
        }
    }

    /**
     * Check if given number is prime number.
     *
     * @param n number to check.
     * @return {@code true} of {@code false} if given number is prime number or not.
     */
    private boolean isPrime(int n) {
        boolean result = n == 2;
        boolean mayBe = n > 2;
        if (!result && mayBe) {
            int max = (int) Math.ceil(Math.sqrt(n));
            for (int i = 2; mayBe && i <= max; i++) {
                mayBe = n % i != 0;
            }
            result = mayBe;
        }
        return result;
    }

}
