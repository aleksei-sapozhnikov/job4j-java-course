package ru.job4j.coffee;

/**
 * Exception thrown if not enough money for the operation.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
class NotEnoughMoneyException extends Exception {

    /**
     * Constructor.
     *
     * @param msg message to show.
     */
    NotEnoughMoneyException(String msg) {
        super(msg);
    }
}
