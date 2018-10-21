package ru.job4j.streams.tracker;

/**
 * Runtime exception thrown when entered action key is not in the possible action keys range.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 23.01.2018
 */
public class MenuActionOutOfRangeException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param msg Message to be shown.
     */
    public MenuActionOutOfRangeException(String msg) {
        super(msg);
    }
}
