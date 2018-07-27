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
     * Directory where views are stored.
     */
    protected static final String VIEWS_DIR = "/WEB-INF/views";
    /**
     * String parameter name to get/set context path.
     */
    protected static final String PARAM_URI_CONTEXT_PATH = "context";
    /**
     * Logic layer object - validator.
     */
    protected static final Validator<User> VALIDATOR = DatabaseValidator.getInstance();
    /**
     * Logic layer object - servlet actions dispatch.
     */
    protected static final ActionsDispatch DISPATCH = new ActionsDispatch(VALIDATOR).initialize();
    /**
     * String  parameter for an error message object.
     */
    protected static final String PARAM_ERROR = "error";
    /**
     * String parameters names for user and user attributes.
     */
    protected static final String PARAM_USER = "user";
    protected static final String PARAM_USER_ID = "id";
    protected static final String PARAM_USER_LOGIN = "login";
    protected static final String PARAM_USER_PASSWORD = "password";
    /**
     * String parameter name for a user logged in current session.
     */
    protected static final String PARAM_LOGGED_USER = "loggedUser";
    /**
     * String parameters names for objects stored in system.
     */
    protected static final String PARAM_ALL_USERS = "users";
    /**
     * String parameters names for URI leading to servlets.
     */
    protected static final String PARAM_URI_CREATE_USER = "create";
    protected static final String PARAM_ALL_ROLES = "roles";
    /**
     * String attributes with URI's leading to servlets.
     */
    protected static final String URI_CREATE_USER = "/create";
    protected static final String PARAM_URI_UPDATE_USER = "update";
    protected static final String PARAM_URI_DELETE_USER = "delete";
    protected static final String PARAM_URI_LOGIN = "login";
    protected static final String PARAM_URI_LOGOUT = "logout";
    /**
     * String attributes with JSP pages.
     */
    protected static final String JSP_CREATE_USER = "/create.jsp";
    protected static final String URI_UPDATE_USER = "/update";
    protected static final String URI_DELETE_USER = "/delete";
    protected static final String URI_LOGIN = "/login";
    protected static final String URI_LOGOUT = "/logout";
    protected static final String JSP_UPDATE_USER = "/update.jsp";
    protected static final String JSP_LIST_USERS = "/list.jsp";
    protected static final String JSP_LOGIN_PAGE = "/login.jsp";
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
