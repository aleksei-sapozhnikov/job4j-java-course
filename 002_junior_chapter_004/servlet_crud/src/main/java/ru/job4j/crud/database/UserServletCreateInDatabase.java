package ru.job4j.crud.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.abstractclasses.AbstractUserServletCreate;

/**
 * Presentation layer "create" servlet. Shows form to
 * add user and creates new user in store.
 * <p>
 * Works with database store.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserServletCreateInDatabase extends AbstractUserServletCreate {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserServletCreateInDatabase.class);

    /**
     * Constructs new servlet object instance.
     */
    public UserServletCreateInDatabase() {
        super(UserValidatorInDatabase.getInstance());
    }

}
