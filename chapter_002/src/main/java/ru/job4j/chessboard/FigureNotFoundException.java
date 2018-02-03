package ru.job4j.chessboard;

/**
 * Checked exception thrown when figure is not found in the position.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.02.2018
 */

class FigureNotFoundException extends Exception {

    /**
     * Constructor.
     *
     * @param msg Message to be shown.
     */
    FigureNotFoundException(String msg) {
        super(msg);
    }

}
