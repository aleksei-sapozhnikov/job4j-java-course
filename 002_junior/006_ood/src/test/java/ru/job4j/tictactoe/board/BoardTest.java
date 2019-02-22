package ru.job4j.tictactoe.board;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.tictactoe.WrongInputException;
import ru.job4j.tictactoe.cell.ICell;
import ru.job4j.tictactoe.cell.ICellFactory;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BoardTest {

    private final ICell free = Mockito.mock(ICell.class);
    private final ICell markX = Mockito.mock(ICell.class);
    private final ICell markO = Mockito.mock(ICell.class);

    private final ICellFactory factory = Mockito.mock(ICellFactory.class);

    public BoardTest() {
        this.initCells();
        this.initFactory();
    }

    private void initCells() {
        Mockito.when(this.free.hasMarkX()).thenReturn(false);
        Mockito.when(this.free.hasMarkO()).thenReturn(false);
        Mockito.when(this.markX.hasMarkX()).thenReturn(true);
        Mockito.when(this.markX.hasMarkO()).thenReturn(false);
        Mockito.when(this.markO.hasMarkX()).thenReturn(false);
        Mockito.when(this.markO.hasMarkO()).thenReturn(true);
    }

    private void initFactory() {
        Mockito.when(this.factory.getFree()).thenReturn(this.free);
        Mockito.when(this.factory.getMarkX()).thenReturn(this.markX);
        Mockito.when(this.factory.getMarkO()).thenReturn(this.markO);
    }

    @Test
    public void whenSetMarkXThenCellMarked() throws WrongInputException {
        Board board = new Board(2, 3, this.factory);
        board.setMarkX(1, 2);
        ICell[][] expected = {
                {this.free, this.free, this.free},
                {this.free, this.free, this.markX}
        };
        assertThat(board.isMarkX(1, 2), is(true));
        assertThat(board.isMarkO(1, 2), is(false));
        assertThat(board.getCells(), is(expected));
    }

    @Test
    public void whenSetMarkOThenCellMarked() throws WrongInputException {
        Board board = new Board(2, 3, this.factory);
        board.setMarkO(0, 2);
        ICell[][] expected = {
                {this.free, this.free, this.markO},
                {this.free, this.free, this.free}
        };
        assertThat(board.isMarkX(0, 2), is(false));
        assertThat(board.isMarkO(0, 2), is(true));
        assertThat(board.getCells(), is(expected));
    }

    @Test
    public void whenConstructedThenReturnsFreeCells() {
        Board board = new Board(2, 3, this.factory);
        ICell[][] expected = {
                {this.free, this.free, this.free},
                {this.free, this.free, this.free}
        };
        assertThat(board.getCells(), is(expected));
    }

    @Test(expected = WrongInputException.class)
    public void whenSetMarkXOutOfBoundsThenWrongInputException() throws WrongInputException {
        Board board = new Board(2, 3, this.factory);
        board.setMarkX(5, 3);
    }

    @Test(expected = WrongInputException.class)
    public void whenSetMarkOOutOfBoundsThenWrongInputException() throws WrongInputException {
        Board board = new Board(2, 3, this.factory);
        board.setMarkO(5, 3);
    }

    @Test(expected = WrongInputException.class)
    public void whenIsMarkXOutOfBoundsThenWrongInputException() throws WrongInputException {
        Board board = new Board(2, 3, this.factory);
        board.isMarkX(5, 3);
    }

    @Test(expected = WrongInputException.class)
    public void whenIsMarkOOutOfBoundsThenWrongInputException() throws WrongInputException {
        Board board = new Board(2, 3, this.factory);
        board.isMarkO(5, 3);
    }
}