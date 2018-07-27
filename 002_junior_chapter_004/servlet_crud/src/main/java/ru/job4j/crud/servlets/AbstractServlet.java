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
     * Main directory where views are stored.
     */
    protected static final String VIEWS_DIR = "/WEB-INF/views";
    /**
     * Logic layer object - validator.
     */
    protected static final Validator<User> VALIDATOR = DatabaseValidator.getInstance();
    /**
     * Logic layer object - servlet actions dispatch.
     */
    protected static final ActionsDispatch DISPATCH = new ActionsDispatch(VALIDATOR).initialize();

    /**
     * Parameter of context path.
     */
    protected static final String PARAM_URI_CONTEXT_PATH = "context";
    /**
     * Request parameter for a current user in system (e.g. user we are going to update)
     */
    protected static final String PARAM_USER = "user";
    /**
     * Request parameter for a user login.
     */
    protected static final String PARAM_USER_LOGIN = "login";
    /**
     * Request parameter for a user login.
     */
    protected static final String PARAM_USER_PASSWORD = "password";
    /**
     * Request parameter for all users in system.
     */
    protected static final String PARAM_ALL_USERS = "users";
    /**
     * Request parameter for all roles existing in the system.
     */
    protected static final String PARAM_ALL_ROLES = "roles";
    /**
     * Request parameter for an error.
     */
    protected static final String PARAM_ERROR = "error";

    /**
     * Parameter for a logged user in session.
     */
    protected static final String PARAM_LOGGED_USER = "loggedUser";

    /**
     * Parameter name for create user uri.
     */
    protected static final String PARAM_URI_CREATE_USER = "create";
    /**
     * Parameter name for create user uri.
     */
    protected static final String PARAM_URI_UPDATE_USER = "update";
    /**
     * Parameter name for create user uri.
     */
    protected static final String PARAM_URI_DELETE_USER = "delete";
    /**
     * Parameter name for create user uri.
     */
    protected static final String PARAM_URI_LOGIN = "login";
    /**
     * Parameter name for create user uri.
     */
    protected static final String PARAM_URI_LOGOUT = "logout";


    /**
     * Name of jsp page to CREATE user.
     */
    protected static final String JSP_CREATE_USER = "/create.jsp";
    /**
     * Name of jsp page to UPDATE user.
     */
    protected static final String JSP_UPDATE_USER = "/update.jsp";
    /**
     * Name of jsp page to LIST ALL users.
     */
    protected static final String JSP_LIST_USERS = "/list.jsp";
    /**
     * Name of jsp page to show LOGIN page.
     */
    protected static final String JSP_LOGIN_PAGE = "/login.jsp";

    /**
     * URI to CREATE user.
     */
    protected static final String URI_CREATE_USER = "/create";
    /**
     * URI to UPDATE user.
     */
    protected static final String URI_UPDATE_USER = "/update";
    /**
     * URI to DELETE user.
     */
    protected static final String URI_DELETE_USER = "/delete";
    /**
     * URI to get LOGIN page.
     */
    protected static final String URI_LOGIN = "/login";
    /**
     * URI to get LOGOUT page.
     */
    protected static final String URI_LOGOUT = "/logout";

    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractServlet.class);

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
