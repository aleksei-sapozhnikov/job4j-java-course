package ru.job4j.iterator.convert;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Converter {

    /**
     * Converts iterator of sub-iterators into iterator that goes through all elements in sub-iterators.
     *
     * @param itOut iterator of sub-iterators.
     * @return iterator that goes through all elements in sub-iterators.
     */
    Iterator<Integer> convert(Iterator<Iterator<Integer>> itOut) {
        return new Iterator<Integer>() {

            /**
             * Current sub-iterator which will give the next Integer.
             */
            private Iterator<Integer> itCurrent = itOut.hasNext() ? itOut.next() : null;

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                return this.itCurrent != null && this.itCurrent.hasNext();
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public Integer next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                int result = itCurrent.next();
                while (itOut.hasNext() && !this.itCurrent.hasNext()) {
                    this.itCurrent = itOut.next();
                }
                return result;
            }
        };
    }

}