package ru.job4j.bomberman;

/**
 * Is thrown when given coordinates do not point to any
 * cell in board.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 26.04.2018
 */
public class WrongCoordinatesException extends Exception {

    /**
     * Constructs new exception with message.
     *
     * @param msg message.
     */
    public WrongCoordinatesException(String msg) {
        super(msg);
    }

}
