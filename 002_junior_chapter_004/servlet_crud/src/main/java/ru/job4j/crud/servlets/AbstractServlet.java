package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.logic.DatabaseValidator;
import ru.job4j.crud.logic.Validator;
import ru.job4j.crud.model.User;

import javax.servlet.http.HttpServlet;

/**
 * General User HttpServlet class. Holds methods and fields needed
 * for every servlet to make interaction with user through html.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public abstract class AbstractServlet extends HttpServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractServlet.class);
    /**
     * Main directory where views are stored.
     */
    private static final String VIEWS_DIR = "/WEB-INF/views";
    /**
     * Logic layer object - validator.
     */
    protected static final Validator<User> VALIDATOR = DatabaseValidator.getInstance();
    /**
     * Logic layer object - servlet actions dispatch.
     */
    protected static final ActionsDispatch DISPATCH = new ActionsDispatch(VALIDATOR).init();
    /**
     * String to name logged user in session.
     */
    protected static final String USER_LOGGED_IN_SESSION = "loggedUser";
    /**
     * String to name logged user in session.
     */
    protected static final String USER_LOGGED_IN_SESSION_INFO = "loggedUserInfo";

    /**
     * Returns views directory path.
     *
     * @return Views directory path.
     */
    protected String getViewsDir() {
        return VIEWS_DIR;
    }

    /**
     * Is called when servlet stops working.
     * Closes connection to database.
     */
    @Override
    public void destroy() {
        try {
            VALIDATOR.close();
        } catch (Exception e) {
            LOG.error(e.getStackTrace());
        }
    }

}
