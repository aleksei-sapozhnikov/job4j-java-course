package ru.job4j.crud.collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.abstractclasses.AbstractUserServletCreate;

/**
 * Presentation layer "create" servlet. Shows form to
 * add user and creates new user in store.
 * <p>
 * Works with collection store.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserServletCreateInCollection extends AbstractUserServletCreate {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserServletCreateInCollection.class);

    /**
     * Constructs new servlet object instance.
     */
    public UserServletCreateInCollection() {
        super(UserValidatorInCollection.getInstance());
    }

}
