package ru.job4j.crud.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.abstractclasses.AbstractUserServletShowDelete;

/**
 * Presentation layer "show and delete" servlet. Shows all users
 * currently in store and deletes them.
 * <p>
 * Works with database store.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserServletShowDeleteInDatabase extends AbstractUserServletShowDelete {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserServletShowDeleteInDatabase.class);

    /**
     * Constructs new servlet object instance.
     */
    public UserServletShowDeleteInDatabase() {
        super(UserValidatorInDatabase.getInstance());
    }

}
