package ru.job4j.crud.collection;

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
public class UserValidatorInCollection extends AbstractUserValidator {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserValidatorInCollection.class);
    /**
     * Instance field.
     */
    private static UserValidatorInCollection instance = new UserValidatorInCollection();

    /**
     * Constructs this class singleton instance.
     */
    private UserValidatorInCollection() {
        super(UserStoreInCollection.getInstance());
    }

    /**
     * Returns this class instance.
     *
     * @return This class instance.
     */
    public static UserValidatorInCollection getInstance() {
        return instance;
    }


}
