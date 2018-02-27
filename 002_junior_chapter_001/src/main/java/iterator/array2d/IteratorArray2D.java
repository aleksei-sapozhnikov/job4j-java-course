package iterator.array2d;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator for the 2-dimensional array (including jagged arrays).
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 26.02.2018
 */
public class IteratorArray2D implements Iterator<Integer> {

    /**
     * Current cursor outer array position(row of matrix).
     */
    private int row = 0;

    /**
     * Current cursor inner position (column of matrix).
     */
    private int column = 0;

    /**
     * Array to iterate.
     */
    private final int[][] values;

    IteratorArray2D(final int[][] values) {
        this.values = values;
        this.moveCursorToNext();
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
        return this.row < this.values.length
                && this.column < this.values[this.row].length;
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
        Integer result = this.values[this.row][this.column];
        this.column++;
        this.moveCursorToNext();
        return result;
    }

    /**
     * Move cursor to the next element.
     */
    private void moveCursorToNext() {
        if (this.column >= this.values[this.row].length) {
            this.column = 0;
            this.row++;
        }
        while (this.row < this.values.length && this.column >= this.values[this.row].length) {
            this.row++;
        }
    }
}
