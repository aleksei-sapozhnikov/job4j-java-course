package ru.job4j.chessboard;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Board class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 04.02.2018
 */
public class BoardTest {

    /**
     * Test move() method.
     */
    @Test
    public void whenLegalMoveThenTrue() throws ImpossibleMoveException, OccupiedWayException, FigureNotFoundException {
        Board board = new Board();
        board.add(new Bishop(new Cell(1, 1)));
        boolean result = board.move(new Cell(1, 1), new Cell(3, 3));
        assertThat(result, is(true));
    }

    @Test(expected = ImpossibleMoveException.class)
    public void whenImpossibleMoveThenImpossibleMoveException() throws ImpossibleMoveException, OccupiedWayException, FigureNotFoundException {
        Board board = new Board();
        board.add(new Bishop(new Cell(1, 1)));
        board.move(new Cell(1, 1), new Cell(2, 3));
    }

    @Test(expected = OccupiedWayException.class)
    public void whenMoveToOccupiedCellThenOccupiedWayException() throws ImpossibleMoveException, OccupiedWayException, FigureNotFoundException {
        Board board = new Board();
        board.add(new Bishop(new Cell(1, 1)));
        board.add(new Bishop(new Cell(3, 3)));
        board.move(new Cell(1, 1), new Cell(3, 3));
    }

    @Test(expected = FigureNotFoundException.class)
    public void whenMoveFromEmptyCellThenFigureNotFoundException() throws ImpossibleMoveException, OccupiedWayException, FigureNotFoundException {
        Board board = new Board();
        board.add(new Bishop(new Cell(1, 1)));
        board.move(new Cell(2, 1), new Cell(4, 3));
    }


}