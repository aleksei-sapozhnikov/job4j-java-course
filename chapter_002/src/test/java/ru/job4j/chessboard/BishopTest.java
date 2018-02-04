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
    public void whenLegalMoveUpAndRightThenWay() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(3, 2));
        Cell[] result = bishop.way(new Cell(3, 2), new Cell(6, 5));
        Cell[] expected = new Cell[]{new Cell(4, 3), new Cell(5, 4), new Cell(6, 5)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenLegalMoveUpAndLeftThenWay() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(1, 2));
        Cell[] result = bishop.way(new Cell(1, 2), new Cell(2, 1));
        Cell[] expected = new Cell[]{new Cell(2, 1)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenLegalMoveDownAndRightThenWay() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(0, 0));
        Cell[] result = bishop.way(new Cell(0, 0), new Cell(-4, 4));
        Cell[] expected = new Cell[]{new Cell(-1, 1), new Cell(-2, 2), new Cell(-3, 3), new Cell(-4, 4)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenLegalMoveDownAndLeftThenWay() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(4, 0));
        Cell[] result = bishop.way(new Cell(4, 0), new Cell(2, -2));
        Cell[] expected = new Cell[]{new Cell(3, -1), new Cell(2, -2)};
        assertThat(result, is(expected));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenIllegalMoveUpThenImpossibleMoveException() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(1, 2));
        bishop.way(new Cell(1, 2), new Cell(2, 4));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenIllegalMoveDownThenImpossibleMoveException() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(1, 2));
        bishop.way(new Cell(1, 2), new Cell(1, 0));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenIllegalMoveRightThenImpossibleMoveException() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(1, 2));
        bishop.way(new Cell(1, 2), new Cell(6, -2));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenIllegalMoveLeftThenImpossibleMoveException() throws ImpossibleMoveException {
        Bishop bishop = new Bishop(new Cell(1, 2));
        bishop.way(new Cell(1, 2), new Cell(-2, 1));
    }
}