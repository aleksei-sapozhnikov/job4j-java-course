package ru.job4j.functional.tracker;

/**
 * Runtime exception.
 * When item with id entered by user is not found in the tracker.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 23.01.2018
 */
public class NoSuchIdException extends RuntimeException {

    public NoSuchIdException(String msg) {
        super(msg);
    }

}
