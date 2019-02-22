package ru.job4j.tictactoe.gamelogic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.tictactoe.WrongInputException;
import ru.job4j.tictactoe.board.IBoard;
import ru.job4j.tictactoe.cell.ICell;
import ru.job4j.tictactoe.player.IPlayer;
import ru.job4j.tictactoe.printer.IPrinter;
import ru.job4j.tictactoe.winchecker.IWinChecker;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

public class GameLogicTest {

    private final IPlayer first = Mockito.mock(IPlayer.class);
    private final IPlayer second = Mockito.mock(IPlayer.class);
    private final IPlayer.Move move = Mockito.mock(IPlayer.Move.class);
    private final IBoard board = Mockito.mock(IBoard.class);
    private final IWinChecker winChecker = Mockito.mock(IWinChecker.class);
    private final IPrinter printer = Mockito.mock(IPrinter.class);

    @Before
    public void initBasicStubs() throws Exception {
        Mockito.when(this.board.getCells()).thenReturn(new ICell[0][0]);
        Mockito.when(this.first.doMove(any(ICell[][].class))).thenReturn(this.move);
        Mockito.when(this.second.doMove(any(ICell[][].class))).thenReturn(this.move);
        Mockito.when(this.move.getIHeight()).thenReturn(0);
        Mockito.when(this.move.getIWidth()).thenReturn(0);
        Mockito.when(this.winChecker.hasGap(any(ICell[][].class))).thenReturn(true);
    }

    @Test
    public void whenFirstPlayerMovesAndWinsThenIsWinnerFirst() throws Exception {
        GameLogic game = new GameLogic(this.first, this.second, this.board, this.winChecker, this.printer);
        assertTrue(game.canContinue());
        Mockito.when(this.winChecker.isWinnerX(any(ICell[][].class))).thenReturn(true);
        game.nextMove();
        assertFalse(game.canContinue());
        assertTrue(game.isWinnerFirst());
    }

    @Test
    public void whenSecondPlayerMovesAndWinsThenIsWinnerSecond() throws Exception {
        GameLogic game = new GameLogic(this.first, this.second, this.board, this.winChecker, this.printer);
        assertTrue(game.canContinue());
        Mockito.when(this.winChecker.isWinnerX(any(ICell[][].class))).thenReturn(false); // first player
        Mockito.when(this.winChecker.isWinnerO(any(ICell[][].class))).thenReturn(true); // second player
        game.nextMove();
        assertTrue(game.canContinue());
        game.nextMove();
        assertFalse(game.canContinue());
        assertTrue(game.isWinnerSecond());
    }

    @Test
    public void whenPrintBoardThenPrintExistingBoard() {
        ICell[][] cells = new ICell[2][3];
        Mockito.when(this.board.getCells()).thenReturn(cells);
        Mockito.when(this.printer.print(cells)).thenReturn("printed");
        GameLogic game = new GameLogic(this.first, this.second, this.board, this.winChecker, this.printer);
        assertThat(game.printBoard(), is("printed"));
    }

    @Test(expected = WrongInputException.class)
    public void whenPlayerDoesMoveOutOnMarkedCellThenWrongInputException() throws Exception {
        Mockito.when(this.board.isMarkX(0, 0)).thenReturn(true);
        Mockito.when(this.move.getIWidth()).thenReturn(0);
        Mockito.when(this.move.getIWidth()).thenReturn(0);
        GameLogic game = new GameLogic(this.first, this.second, this.board, this.winChecker, this.printer);
        game.nextMove();
    }
}