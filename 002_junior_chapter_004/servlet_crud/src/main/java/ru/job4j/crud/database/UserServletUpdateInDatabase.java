package ru.job4j.crud.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.abstractclasses.AbstractUserServletUpdate;

/**
 * Presentation layer "show a" servlet. Shows form to
 * update user fields and updates them.
 * <p>
 * Works with database store.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserServletUpdateInDatabase extends AbstractUserServletUpdate {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserServletUpdateInDatabase.class);

    /**
     * Constructs new servlet object instance.
     */
    public UserServletUpdateInDatabase() {
        super(UserValidatorInDatabase.getInstance());
    }

}
