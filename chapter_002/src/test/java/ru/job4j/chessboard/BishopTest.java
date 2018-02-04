package ru.job4j.chessboard;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Bishop class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.02.2018
 */
public class BishopTest {

    /**
     * Test getPosition() method.
     */
    @Test
    public void whenGetPositionThenPosition() {
        Bishop bishop = new Bishop(new Cell(1, 2));
        Cell result = bishop.getPosition();
        Cell expected = new Cell(1, 2);
        assertThat(result, is(expected));
    }

    /**
     * Test way() method.
     */
    @Test
    public void whenMoveThreeUpwardsGoodThenWay() throws ImpossibleMoveException {
        Cell source = new Cell(3, 3);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(6, 6);
        Cell[] result = bishop.way(source, dest);
        Cell[] expected = new Cell[]{new Cell(4, 4), new Cell(5, 5), new Cell(6, 6)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenMoveOneUpwardsGoodThenWay() throws ImpossibleMoveException {
        Cell source = new Cell(0, 0);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(1, 1);
        Cell[] result = bishop.way(source, dest);
        Cell[] expected = new Cell[]{new Cell(1, 1)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenMoveTwoDownwardsGoodThenWay() throws ImpossibleMoveException {
        Cell source = new Cell(3, 5);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(1, 3);
        Cell[] result = bishop.way(source, dest);
        Cell[] expected = new Cell[]{new Cell(2, 4), new Cell(1, 3)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenMoveOneDownWardsGoodThenWay() throws ImpossibleMoveException {
        Cell source = new Cell(1, 2);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(0, 1);
        Cell[] result = bishop.way(source, dest);
        Cell[] expected = new Cell[]{new Cell(0, 1)};
        assertThat(result, is(expected));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenMoveToTheSameCellThenImpossibleMoveException() throws ImpossibleMoveException {
        Cell source = new Cell(1, 1);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(1, 1);
        bishop.way(source, dest);
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenMoveTwoUpWrongThenImpossibleMoveException() throws ImpossibleMoveException {
        Cell source = new Cell(1, 2);
        Bishop bishop = new Bishop(source);
        Cell dest = new Cell(2, 0);
        bishop.way(source, dest);
    }

    @Test
    public void whenNewDestinationCellThenBishopInDestinationCell() {
        Bishop bishop = new Bishop(new Cell(3, 4));
        bishop = bishop.copy(new Cell(1, 2));
        Cell result = bishop.getPosition();
        Cell expected = new Cell(1, 2);
        assertThat(result, is(expected));
    }
}