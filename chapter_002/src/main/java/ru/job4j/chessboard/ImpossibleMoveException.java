package ru.job4j.chessboard;

/**
 * Checked exception thrown when the move can't be done by this type of figure.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.02.2018
 */
class ImpossibleMoveException extends Exception {

    /**
     * Constructor.
     *
     * @param msg Message to be shown.
     */
    ImpossibleMoveException(String msg) {
        super(msg);
    }
}
