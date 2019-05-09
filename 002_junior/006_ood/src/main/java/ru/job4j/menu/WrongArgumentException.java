package ru.job4j.menu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Checked exception showing that value given
 * to method was wrong (illegal).
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class WrongArgumentException extends Exception {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(WrongArgumentException.class);

    /**
     * Constructor.
     *
     * @param message Message.
     */
    public WrongArgumentException(String message) {
        super(message);
    }
}
