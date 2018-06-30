package ru.job4j.crud.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.store.UserStoreInDatabase;

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
public class UserValidatorInDatabase extends AbstractUserValidator {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserValidatorInDatabase.class);
    /**
     * Instance field.
     */
    private static UserValidatorInDatabase instance = new UserValidatorInDatabase();

    /**
     * Constructs this class singleton instance.
     */
    private UserValidatorInDatabase() {
        super(UserStoreInDatabase.getInstance());
    }

    /**
     * Returns this class instance.
     *
     * @return This class instance.
     */
    public static UserValidatorInDatabase getInstance() {
        return instance;
    }
}
