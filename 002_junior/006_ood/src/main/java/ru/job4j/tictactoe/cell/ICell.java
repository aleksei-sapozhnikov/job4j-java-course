package ru.job4j.tictactoe.cell;

/**
 * TicTacToe cell object.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface ICell {
    /**
     * Returns if cell is marked with "X".
     *
     * @return <tt>true</tt> if mark is "X", otherwise "false".
     */
    boolean hasMarkX();

    /**
     * Returns if cell is marked with "O".
     *
     * @return <tt>true</tt> if mark is "O", otherwise "false".
     */
    boolean hasMarkO();
}
