package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Presentation layer servlet. Gets requests for actions:
 * create, update, delete user or show all users. Shows result.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserServletDatabase extends AbstractUserServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserServletDatabase.class);

    /**
     * Constructs object of this class.
     */
    public UserServletDatabase() {
        super(UserValidatorDatabase.getInstance());
    }


}
