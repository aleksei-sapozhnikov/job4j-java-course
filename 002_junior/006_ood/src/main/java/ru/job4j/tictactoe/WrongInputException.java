package ru.job4j.tictactoe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Checked analog to IllegalArgumentException.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class WrongInputException extends Exception {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(WrongInputException.class);

    /**
     * Constructs exception with message.
     *
     * @param message Message.
     */
    public WrongInputException(String message) {
        super(message);
    }
}
