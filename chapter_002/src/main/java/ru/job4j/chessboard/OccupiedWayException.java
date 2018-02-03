package ru.job4j.chessboard;

/**
 * Checked exception thrown when move can be made but the way is blocked.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.02.2018
 */
class OccupiedWayException extends Exception {

    /**
     * Constructor.
     *
     * @param msg Message to be shown.
     */
    OccupiedWayException(String msg) {
        super(msg);
    }
}
