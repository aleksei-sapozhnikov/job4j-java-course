package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logic layer for Users store. Validates each object before adding it to storage.
 * <p>
 * Singleton class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserValidatorCollection extends AbstractUserValidator {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserValidatorCollection.class);
    /**
     * Instance field.
     */
    private static UserValidatorCollection instance = new UserValidatorCollection();

    /**
     * Constructs this class singleton instance.
     */
    private UserValidatorCollection() {
        super(UserStoreCollection.getInstance());
    }

    /**
     * Returns this class instance.
     *
     * @return This class instance.
     */
    public static UserValidatorCollection getInstance() {
        return instance;
    }


}
