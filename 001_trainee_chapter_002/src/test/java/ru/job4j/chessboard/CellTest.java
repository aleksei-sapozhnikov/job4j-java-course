package ru.job4j.chessboard;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CellTest {

    /**
     * Test horizontalDistanceTo() method.
     */
    @Test
    public void whenOtherCellToUpRightThenHorizontalDistancePositive() {
        Cell beg = new Cell(1, 2);
        Cell end = new Cell(4, 6);
        int result = beg.horizontalDistanceTo(end);
        int expect = 3;
        assertThat(result, is(expect));
    }

    @Test
    public void whenOtherCellToUpLeftThenHorizontalDistanceNegative() {
        Cell beg = new Cell(4, -3);
        Cell end = new Cell(0, 2);
        int result = beg.horizontalDistanceTo(end);
        int expect = -4;
        assertThat(result, is(expect));
    }

    @Test
    public void whenOtherCellToDownRightThenHorizontalDistancePositive() {
        Cell beg = new Cell(-4, 3);
        Cell end = new Cell(5, 0);
        int result = beg.horizontalDistanceTo(end);
        int expect = 9;
        assertThat(result, is(expect));
    }

    @Test
    public void whenOtherCellToDownLeftThenHorizontalDistanceNegative() {
        Cell beg = new Cell(1, 3);
        Cell end = new Cell(0, 0);
        int result = beg.horizontalDistanceTo(end);
        int expect = -1;
        assertThat(result, is(expect));
    }

    @Test
    public void whenOtherCellSameHorizontalCoordinateThenHorizontalDistanceZero() {
        Cell beg = new Cell(1, 3);
        Cell end = new Cell(1, 5);
        int result = beg.horizontalDistanceTo(end);
        int expect = 0;
        assertThat(result, is(expect));
    }

    /**
     * Test verticalDistanceTo() method.
     */
    @Test
    public void whenOtherCellToUpRightThenVerticalDistancePositive() {
        Cell beg = new Cell(1, 2);
        Cell end = new Cell(4, 6);
        int result = beg.verticalDistanceTo(end);
        int expect = 4;
        assertThat(result, is(expect));
    }

    @Test
    public void whenOtherCellToUpLeftThenVerticalDistancePositive() {
        Cell beg = new Cell(4, -3);
        Cell end = new Cell(0, 2);
        int result = beg.verticalDistanceTo(end);
        int expect = 5;
        assertThat(result, is(expect));
    }

    @Test
    public void whenOtherCellToDownRightThenVerticalDistanceNegative() {
        Cell beg = new Cell(-4, 3);
        Cell end = new Cell(5, 0);
        int result = beg.verticalDistanceTo(end);
        int expect = -3;
        assertThat(result, is(expect));
    }

    @Test
    public void whenOtherCellToDownLeftThenVerticalDistanceNegative() {
        Cell beg = new Cell(1, 8);
        Cell end = new Cell(0, -2);
        int result = beg.verticalDistanceTo(end);
        int expect = -10;
        assertThat(result, is(expect));
    }

    @Test
    public void whenOtherCellSameVerticalDistanceThenVerticalDistanceZero() {
        Cell beg = new Cell(1, 8);
        Cell end = new Cell(0, 8);
        int result = beg.verticalDistanceTo(end);
        int expect = 0;
        assertThat(result, is(expect));
    }

    /**
     * Test step() method.
     */
    @Test
    public void whenStepUpThenNextCell() {
        Cell beg = new Cell(1, 2);
        Cell result = beg.step(Cell.HorizontalDirection.NONE, Cell.VerticalDirection.UP);
        Cell expect = new Cell(1, 3);
        assertThat(result, is(expect));
    }

    @Test
    public void whenStepDownThenNextCell() {
        Cell beg = new Cell(1, 2);
        Cell result = beg.step(Cell.HorizontalDirection.NONE, Cell.VerticalDirection.DOWN);
        Cell expect = new Cell(1, 1);
        assertThat(result, is(expect));
    }

    @Test
    public void whenStepRightThenNextCell() {
        Cell beg = new Cell(1, 2);
        Cell result = beg.step(Cell.HorizontalDirection.RIGHT, Cell.VerticalDirection.NONE);
        Cell expect = new Cell(2, 2);
        assertThat(result, is(expect));
    }

    @Test
    public void whenStepLeftThenNextCell() {
        Cell beg = new Cell(1, 2);
        Cell result = beg.step(Cell.HorizontalDirection.LEFT, Cell.VerticalDirection.NONE);
        Cell expect = new Cell(0, 2);
        assertThat(result, is(expect));
    }

    @Test
    public void whenStepRightUpThenNextCell() {
        Cell beg = new Cell(1, 2);
        Cell result = beg.step(Cell.HorizontalDirection.RIGHT, Cell.VerticalDirection.UP);
        Cell expect = new Cell(2, 3);
        assertThat(result, is(expect));
    }

    @Test
    public void whenStepRightDownThenNextCell() {
        Cell beg = new Cell(1, 2);
        Cell result = beg.step(Cell.HorizontalDirection.RIGHT, Cell.VerticalDirection.DOWN);
        Cell expect = new Cell(2, 1);
        assertThat(result, is(expect));
    }

    @Test
    public void whenStepLeftUpThenNextCell() {
        Cell beg = new Cell(1, 2);
        Cell result = beg.step(Cell.HorizontalDirection.LEFT, Cell.VerticalDirection.UP);
        Cell expect = new Cell(0, 3);
        assertThat(result, is(expect));
    }

    @Test
    public void whenStepLeftDownThenNextCell() {
        Cell beg = new Cell(1, 2);
        Cell result = beg.step(Cell.HorizontalDirection.LEFT, Cell.VerticalDirection.DOWN);
        Cell expect = new Cell(0, 1);
        assertThat(result, is(expect));
    }

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