package ru.job4j.crud.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.AbstractUserValidator;

/**
 * Logic layer for Users store. Validates each object before adding it to storage.
 * <p>
 * Singleton class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserValidatorDatabase extends AbstractUserValidator {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserValidatorDatabase.class);
    /**
     * Instance field.
     */
    private static UserValidatorDatabase instance = new UserValidatorDatabase();

    /**
     * Constructs this class singleton instance.
     */
    private UserValidatorDatabase() {
        super(UserStoreDatabase.getInstance());
    }

    /**
     * Returns this class instance.
     *
     * @return This class instance.
     */
    public static UserValidatorDatabase getInstance() {
        return instance;
    }
}
