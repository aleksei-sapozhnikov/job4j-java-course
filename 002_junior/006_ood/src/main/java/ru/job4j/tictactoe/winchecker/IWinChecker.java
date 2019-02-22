package ru.job4j.tictactoe.winchecker;

import ru.job4j.tictactoe.cell.ICell;

/**
 * Object checking if there is winner combination or if game can be continued.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 22.04.2018
 */
public interface IWinChecker {
    /**
     * Checks if "X" won the game.
     *
     * @return <tt>true</tt> if "X" won, <tt>false</tt> if not.
     */
    boolean isWinnerX(ICell[][] table);

    /**
     * Checks if "O" won the game.
     *
     * @return <tt>true</tt> if "O" won, <tt>false</tt> if not.
     */
    boolean isWinnerO(ICell[][] table);

    /**
     * Checks if there is a field without mark on the table.
     *
     * @return <tt>true</tt> if found empty cell, <tt>false</tt> if not found.
     */
    boolean hasGap(ICell[][] table);
}
