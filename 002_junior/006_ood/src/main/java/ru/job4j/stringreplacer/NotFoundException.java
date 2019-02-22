package ru.job4j.stringreplacer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Thrown if needed element was not found (not defined by user).
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class NotFoundException extends Exception {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(NotFoundException.class);

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
