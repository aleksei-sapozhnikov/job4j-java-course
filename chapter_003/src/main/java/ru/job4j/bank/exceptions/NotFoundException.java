package ru.job4j.bank.exceptions;

/**
 * Exception thrown if object needed was not found in collection.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class NotFoundException extends Exception {

    /**
     * Constructor.
     *
     * @param msg messae to show.
     */
    public NotFoundException(String msg) {
        super(msg);
    }
}
