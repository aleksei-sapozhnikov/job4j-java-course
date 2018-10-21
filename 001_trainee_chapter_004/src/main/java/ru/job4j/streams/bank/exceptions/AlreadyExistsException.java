package ru.job4j.streams.bank.exceptions;

/**
 * Exception thrown if object that is added into collection already exists in the collection.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class AlreadyExistsException extends Exception {

    /**
     * Constructor.
     *
     * @param msg messae to show.
     */
    public AlreadyExistsException(String msg) {
        super(msg);
    }
}
