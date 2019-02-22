package ru.job4j.tictactoe.gamelogic;

public interface IGameLogic {
    /**
     * Makes next move by player in row.
     *
     * @throws Exception In case of problems.
     */
    void nextMove() throws Exception;

    /**
     * Checks if game can be continued.
     *
     * @return <tt>true</tt> if can continue, <tt>false</tt> if not.
     */
    boolean canContinue();

    /**
     * Returns if first player won the game.
     *
     * @return <tt>true</tt> if won, <tt>false</tt> if not.
     */
    boolean isWinnerFirst();

    /**
     * Returns if second player won the game.
     *
     * @return <tt>true</tt> if won, <tt>false</tt> if not.
     */
    boolean isWinnerSecond();

    /**
     * Retusn current board state as String object.
     *
     * @return Current board state as String object.
     */
    String printBoard();
}
