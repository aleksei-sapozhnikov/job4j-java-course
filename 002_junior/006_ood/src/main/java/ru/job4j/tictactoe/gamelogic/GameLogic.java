package ru.job4j.tictactoe.gamelogic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.tictactoe.WrongInputException;
import ru.job4j.tictactoe.board.IBoard;
import ru.job4j.tictactoe.player.IPlayer;
import ru.job4j.tictactoe.printer.IPrinter;
import ru.job4j.tictactoe.winchecker.IWinChecker;

/**
 * Basic tic tac toe game logic.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class GameLogic implements IGameLogic {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(GameLogic.class);
    /**
     * Winner checking logic.
     */
    private final IWinChecker winChecker;
    /**
     * First player object. First player makes the first move.
     */
    private final IPlayer playerFirst;
    /**
     * Second player object.
     */
    private final IPlayer playerSecond;
    /**
     * Table-to-string printer object.
     */
    private final IPrinter printer;
    /**
     * Game board object.
     */
    private final IBoard board;

    /**
     * Flag defining which player is to make a move: first or second.
     */
    private boolean playerFirstMove = true;
    /**
     * Flag showing if current move mark is X or O.
     */
    private boolean markX = true;

    /**
     * Flag showing that there is still place to make a move.
     */
    private boolean hasGap = true;
    /**
     * Flag showing that first player won the game.
     */
    private boolean winnerFirst = false;
    /**
     * Flag showing that second player won the game.
     */
    private boolean winnerSecond = false;

    /**
     * Constructor.
     *
     * @param first      First player. Makes move first.
     * @param second     Second player.
     * @param board      Game board.
     * @param winChecker Win checking logic.
     * @param printer    Board-to-string printer.
     */
    public GameLogic(IPlayer first, IPlayer second, IBoard board, IWinChecker winChecker, IPrinter printer) {
        this.playerFirst = first;
        this.playerSecond = second;
        this.board = board;
        this.winChecker = winChecker;
        this.printer = printer;
    }

    /**
     * Makes next move by player in row.
     *
     * @throws Exception In case of problems.
     */
    @Override
    public void nextMove() throws Exception {
        this.doMove(
                this.playerFirstMove ? this.playerFirst : this.playerSecond,
                this.markX);
        this.markIfIsWinner();
        this.markIfCanContinue();
        this.playerFirstMove = this.switchFlag(this.playerFirstMove);
        this.markX = this.switchFlag(this.markX);
    }

    /**
     * Checks if game can be continued.
     *
     * @return <tt>true</tt> if can continue, <tt>false</tt> if not.
     */
    @Override
    public boolean canContinue() {
        return !this.winnerFirst
                && !this.winnerSecond
                && this.hasGap;
    }

    /**
     * Returns if first player won the game.
     *
     * @return <tt>true</tt> if won, <tt>false</tt> if not.
     */
    @Override
    public boolean isWinnerFirst() {
        return this.winnerFirst;
    }

    /**
     * Returns if second player won the game.
     *
     * @return <tt>true</tt> if won, <tt>false</tt> if not.
     */
    @Override
    public boolean isWinnerSecond() {
        return this.winnerSecond;
    }

    /**
     * Sets flag showing if there is still place to make a move.
     */
    private void markIfCanContinue() {
        this.hasGap = this.winChecker.hasGap(this.board.getCells());
    }

    /**
     * Sets winner flag if one of the players won the game.
     */
    private void markIfIsWinner() {
        if (this.playerFirstMove) {
            this.winnerFirst = this.isWinner(markX);
        } else {
            this.winnerSecond = this.isWinner(markX);
        }
    }

    /**
     * Checks if there is a winner with given marks.
     *
     * @param markX <tt>true</tt> means check X marks, <tt>false</tt> means check O marks.
     * @return <tt>true</tt> if winning combination found, <tt>false if not.</tt>.
     */
    private boolean isWinner(boolean markX) {
        return markX
                ? this.winChecker.isWinnerX(this.board.getCells())
                : this.winChecker.isWinnerO(this.board.getCells());
    }

    /**
     * Makes move defined by player.
     *
     * @param player Player object.
     * @param markX  <tt>true</tt> if winning combination found, <tt>false if not.</tt>.
     * @throws Exception In calse of problems.
     */
    private void doMove(IPlayer player, boolean markX) throws Exception {
        var move = player.doMove(this.board.getCells());
        var iHeight = move.getIHeight();
        var iWidth = move.getIWidth();
        if (!this.isCellFree(iHeight, iWidth)) {
            throw new WrongInputException("Cell is marked already");
        }
        if (markX) {
            this.board.setMarkX(iHeight, iWidth);
        } else {
            this.board.setMarkO(iHeight, iWidth);
        }
    }

    /**
     * Checks if cell is not marked by X or O.
     *
     * @param iHeight Cell height index.
     * @param iWidth  Cell width index.
     * @return <tt>true</tt> if cell is not marked, <tt>false</tt> if not.
     * @throws WrongInputException If given indices are out of board bounds.
     */
    private boolean isCellFree(int iHeight, int iWidth) throws WrongInputException {
        return !this.board.isMarkO(iHeight, iWidth)
                && !this.board.isMarkX(iHeight, iWidth);
    }

    /**
     * Changes boolean flag value to opposite.
     *
     * @param flag Boolean flag value.
     * @return Opposite value.
     */
    private boolean switchFlag(boolean flag) {
        return !flag;
    }

    /**
     * Returns game board as String object.
     *
     * @return String representation of game board.
     */
    @Override
    public String printBoard() {
        return this.printer.print(this.board.getCells());
    }
}
