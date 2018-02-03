package ru.job4j.chessboard;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CellTest {

    /**
     * Test isInRange() method.
     */
    @Test
    public void whenIsInPositiveRangeThenTrue() {
        int xMin = 0, xMax = 5, yMin = 0, yMax = 7;
        Cell cell = new Cell(3, 7);
        boolean result = cell.isInRange(xMin, xMax, yMin, yMax);
        assertThat(result, is(true));
    }

    @Test
    public void whenIsInRangeDifferentSignsThenTrue() {
        int xMin = -5, xMax = 0, yMin = -6, yMax = 3;
        Cell cell = new Cell(-3, 2);
        boolean result = cell.isInRange(xMin, xMax, yMin, yMax);
        assertThat(result, is(true));
    }

    @Test
    public void whenIsNotInRangeByXThenFalse() {
        int xMin = 0, xMax = 5, yMin = 0, yMax = 7;
        Cell cell = new Cell(7, 5);
        boolean result = cell.isInRange(xMin, xMax, yMin, yMax);
        assertThat(result, is(false));
    }

    @Test
    public void whenIsNotInRangeByYThenFalse() {
        int xMin = 0, xMax = 5, yMin = 0, yMax = 7;
        Cell cell = new Cell(3, -6);
        boolean result = cell.isInRange(xMin, xMax, yMin, yMax);
        assertThat(result, is(false));
    }

    /**
     * Test equals() method.
     */
    @Test
    public void whenCellsAreEqualThenTrue() {
        Cell cellOne = new Cell(4, -3);
        Cell cellTwo = new Cell(4, -3);
        boolean result = cellOne.equals(cellTwo);
        assertThat(result, is(true));
    }

    @Test
    public void whenCellsAreNotEqualThenFalse() {
        Cell cellOne = new Cell(-4, 3);
        Cell cellTwo = new Cell(2, 1);
        boolean result = cellOne.equals(cellTwo);
        assertThat(result, is(false));
    }

    /**
     * Test hashCode() method.
     */
    @Test
    public void whenEqualCellsThenTheSameHashcode1() {
        Cell cellOne = new Cell(5, -4);
        Cell cellTwo = new Cell(5, -4);
        boolean result = cellOne.hashCode() == cellTwo.hashCode();
        assertThat(result, is(true));
    }

    @Test
    public void whenEqualCellsThenTheSameHashcode2() {
        Cell cellOne = new Cell(-4, 0);
        Cell cellTwo = new Cell(-4, 0);
        boolean result = cellOne.hashCode() == cellTwo.hashCode();
        assertThat(result, is(true));
    }
}