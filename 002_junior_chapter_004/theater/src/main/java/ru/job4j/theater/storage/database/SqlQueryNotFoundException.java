package ru.job4j.theater.storage.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Exception thrown when query key was not found in the queries map.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class SqlQueryNotFoundException extends RuntimeException {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(SqlQueryNotFoundException.class);

    public SqlQueryNotFoundException(String msg) {
        super(msg);
    }
}
