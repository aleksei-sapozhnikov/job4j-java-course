package ru.job4j.chessboard;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Rook class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.02.2018
 */
public class RookTest {

    /**
     * Test way() method.
     */
    @Test
    public void whenLegalMoveUpThenWay() throws ImpossibleMoveException {
        Rook rook = new Rook(new Cell(3, 2));
        Cell[] result = rook.way(new Cell(3, 2), new Cell(3, 6));
        Cell[] expected = new Cell[]{new Cell(3, 3), new Cell(3, 4), new Cell(3, 5), new Cell(3, 6)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenLegalMoveDownThenWay() throws ImpossibleMoveException {
        Rook rook = new Rook(new Cell(1, 2));
        Cell[] result = rook.way(new Cell(1, 2), new Cell(1, -3));
        Cell[] expected = new Cell[]{new Cell(1, 1), new Cell(1, 0), new Cell(1, -1), new Cell(1, -2), new Cell(1, -3)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenLegalMoveRightThenWay() throws ImpossibleMoveException {
        Rook rook = new Rook(new Cell(0, 0));
        Cell[] result = rook.way(new Cell(0, 0), new Cell(4, 0));
        Cell[] expected = new Cell[]{new Cell(1, 0), new Cell(2, 0), new Cell(3, 0), new Cell(4, 0)};
        assertThat(result, is(expected));
    }

    @Test
    public void whenLegalMoveLeftThenWay() throws ImpossibleMoveException {
        Rook rook = new Rook(new Cell(3, 0));
        Cell[] result = rook.way(new Cell(3, 0), new Cell(-1, 0));
        Cell[] expected = new Cell[]{new Cell(2, 0), new Cell(1, 0), new Cell(0, 0), new Cell(-1, 0)};
        assertThat(result, is(expected));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenIllegalMoveUpRightThenImpossibleMoveException() throws ImpossibleMoveException {
        Rook rook = new Rook(new Cell(1, 2));
        rook.way(new Cell(1, 2), new Cell(2, 4));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenIllegalMoveDownRightThenImpossibleMoveException() throws ImpossibleMoveException {
        Rook rook = new Rook(new Cell(1, 2));
        rook.way(new Cell(1, 2), new Cell(3, -5));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenIllegalMoveUpLeftThenImpossibleMoveException() throws ImpossibleMoveException {
        Rook rook = new Rook(new Cell(1, 2));
        rook.way(new Cell(1, 2), new Cell(-2, 5));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenIllegalMoveDownLeftThenImpossibleMoveException() throws ImpossibleMoveException {
        Rook rook = new Rook(new Cell(1, 2));
        rook.way(new Cell(1, 2), new Cell(-2, 1));
    }
}