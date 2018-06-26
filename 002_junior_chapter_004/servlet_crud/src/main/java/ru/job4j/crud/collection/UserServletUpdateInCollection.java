package ru.job4j.crud.collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.abstractclasses.AbstractUserServletUpdate;

/**
 * Presentation layer "update" servlet. Shows form to
 * update user fields and updates them.
 * <p>
 * Works with collection store.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserServletUpdateInCollection extends AbstractUserServletUpdate {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserServletUpdateInCollection.class);

    /**
     * Constructs new servlet object instance.
     */
    public UserServletUpdateInCollection() {
        super(UserValidatorInCollection.getInstance());
    }

}
