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
     * Test way() method.
     */
    @Test
    public void whenMoveThreeUpwardsGoodThenWay() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(3, 3));
        Cell[] result = bishop.way(new Cell(3, 3), new Cell(6, 6));
        Cell[] expected = new Cell[]{new Cell(4, 4), new Cell(5, 5), new Cell(6, 6)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenMoveOneUpwardsGoodThenWay() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(0, 0));
        Cell[] result = bishop.way(new Cell(0, 0), new Cell(1, 1));
        Cell[] expected = new Cell[]{new Cell(1, 1)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenMoveTwoDownwardsGoodThenWay() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(3, 5));
        Cell[] result = bishop.way(new Cell(3, 5), new Cell(1, 3));
        Cell[] expected = new Cell[]{new Cell(2, 4), new Cell(1, 3)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenMoveOneDownWardsGoodThenWay() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(1, 2));
        Cell[] result = bishop.way(new Cell(1, 2), new Cell(0, 1));
        Cell[] expected = new Cell[]{new Cell(0, 1)};
        assertThat(result, is(expected));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenMoveTwoUpWrongThenImpossibleMoveException() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(1, 2));
        bishop.way(new Cell(1, 2), new Cell(2, 0));
    }
}