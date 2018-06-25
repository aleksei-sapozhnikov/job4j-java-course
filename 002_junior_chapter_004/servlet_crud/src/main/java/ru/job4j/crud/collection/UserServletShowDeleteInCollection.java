package ru.job4j.crud.collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.abstractclasses.AbstractUserServletShowDelete;

/**
 * Presentation layer "show and delete" servlet. Shows all users
 * currently in store and deletes them.
 * <p>
 * Works with collection store.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserServletShowDeleteInCollection extends AbstractUserServletShowDelete {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserServletShowDeleteInCollection.class);

    /**
     * Constructs new servlet object instance.
     */
    public UserServletShowDeleteInCollection() {
        super(UserValidatorInCollection.getInstance());
    }

}
