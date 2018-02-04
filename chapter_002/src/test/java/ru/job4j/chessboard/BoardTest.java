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
    public void whenMoveIntoTheSameCellThenImpossibleMoveException() throws ImpossibleMoveException, OccupiedWayException, FigureNotFoundException {
        Board board = new Board();
        board.add(new Bishop(new Cell(2, 1)));
        board.move(new Cell(2, 1), new Cell(2, 1));
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

    /**
     * Test isAnyFigureInPositions() method.
     */
    @Test
    public void whenIsAnyFigureInPositionsThenTrue() {
        Board board = new Board();
        board.add(new Bishop(new Cell(1, 2)));
        board.add(new Bishop(new Cell(5, 6)));
        board.add(new Bishop(new Cell(0, 2)));
        Cell[] positions = new Cell[]{
                new Cell(3, 6),
                new Cell(5, 6),
                new Cell(0, 3),
        };
        boolean result = board.isAnyFigureInPositions(positions);
        assertThat(result, is(true));
    }

    @Test
    public void whenNoAnyFigureInPositionsThenFalse() {
        Board board = new Board();
        board.add(new Bishop(new Cell(1, 2)));
        board.add(new Bishop(new Cell(5, 6)));
        board.add(new Bishop(new Cell(0, 2)));
        Cell[] positions = new Cell[]{
                new Cell(3, 6),
                new Cell(5, 5),
                new Cell(0, 3),
        };
        boolean result = board.isAnyFigureInPositions(positions);
        assertThat(result, is(false));
    }
}