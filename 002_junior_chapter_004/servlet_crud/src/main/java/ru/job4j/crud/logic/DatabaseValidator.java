package ru.job4j.crud.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.store.DatabaseStore;

/**
 * Logic layer for Users store using database.
 * <p>
 * Validates each object before adding it to storage.
 * <p>
 * Singleton class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class DatabaseValidator extends AbstractValidator {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DatabaseValidator.class);
    /**
     * Instance field.
     */
    private static final DatabaseValidator instance = new DatabaseValidator();

    /**
     * Constructs this class singleton instance.
     */
    private DatabaseValidator() {
        super(DatabaseStore.getInstance());
    }

    /**
     * Returns this class instance.
     *
     * @return This class instance.
     */
    public static DatabaseValidator getInstance() {
        return instance;
    }
}
