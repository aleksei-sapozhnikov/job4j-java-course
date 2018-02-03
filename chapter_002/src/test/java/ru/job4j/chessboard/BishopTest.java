package ru.job4j.chessboard;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BishopTest {

    /**
     * Test way() method.
     */
    @Test
    public void WhenMoveThreeUpwardsGoodThenTrue() throws ImpossibleMoveException {
        Cell source = new Cell(3, 3);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(6, 6);
        Cell[] result = bishop.way(source, dest);
        Cell[] expected = new Cell[]{new Cell(4, 4), new Cell(5, 5), new Cell(6, 6)};
        assertThat(result, is(expected));
    }

    @Test
    public void WhenMoveOneUpwardsGoodThenTrue() throws ImpossibleMoveException {
        Cell source = new Cell(0, 0);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(1, 1);
        Cell[] result = bishop.way(source, dest);
        Cell[] expected = new Cell[]{new Cell(1, 1)};
        assertThat(result, is(expected));
    }

    @Test
    public void WhenMoveTwoDownwardsGoodThenTrue() throws ImpossibleMoveException {
        Cell source = new Cell(3, 3);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(1, 1);
        Cell[] result = bishop.way(source, dest);
        Cell[] expected = new Cell[]{new Cell(2, 2), new Cell(1, 1)};
        assertThat(result, is(expected));
    }

    @Test
    public void WhenMoveOneDownWardsGoodThenTrue() throws ImpossibleMoveException {
        Cell source = new Cell(1, 1);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(0, 0);
        Cell[] result = bishop.way(source, dest);
        Cell[] expected = new Cell[]{new Cell(0, 0)};
        assertThat(result, is(expected));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void WhenMoveToTheSameCellThenImpossibleMoveException() throws ImpossibleMoveException {
        Cell source = new Cell(1, 1);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(1, 1);
        bishop.way(source, dest);
    }

}