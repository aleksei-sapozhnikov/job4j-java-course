package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.User;
import ru.job4j.crud.logic.UserValidatorInCollection;
import ru.job4j.crud.logic.UserValidatorInDatabase;
import ru.job4j.crud.logic.Validator;

import javax.servlet.http.HttpServlet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
     * Name of the url parameter which defines store type used.
     */
    private static final String URL_PARAM_STORE = "store";

    /**
     * Map with user storage objects.
     */
    private static final Map<String, Validator<User>> VALIDATORS = Collections.unmodifiableMap(
            new HashMap<String, Validator<User>>() {
                {
                    put("collection", UserValidatorInCollection.getInstance());
                    put("database", UserValidatorInDatabase.getInstance());
                }
            });
    /**
     * Actions dispatch map.
     */
    private static final Map<String, DispatchServletActions> DISPATCHERS = Collections.unmodifiableMap(
            new HashMap<String, DispatchServletActions>() {
                {
                    put("collection", new DispatchServletActions(UserValidatorInCollection.getInstance()).init());
                    put("database", new DispatchServletActions(UserValidatorInDatabase.getInstance()).init());
                }
            });

    /**
     * Returns views directory path.
     *
     * @return Views directory path.
     */
    protected String getViewsDir() {
        return VIEWS_DIR;
    }

    /**
     * Returns name of the url parameter which defines store type used.
     *
     * @return Name of the url parameter which defines store type used.
     */
    public String getUrlParamStore() {
        return URL_PARAM_STORE;
    }

    /**
     * Returns map with possible logic layer objects, different in
     * stores they use.
     *
     * @return Map with possible logic layer objects, different in
     * stores they use.
     */
    public Map<String, Validator<User>> getValidators() {
        return VALIDATORS;
    }

    /**
     * Returns map with possible actions dispatch objects, different in
     * logic layer objects they use.
     *
     * @return Map with possible actions dispatch objects, different in
     * logic layer objects they use.
     */
    public Map<String, DispatchServletActions> getDispatchers() {
        return DISPATCHERS;
    }

    /**
     * Is called when servlet stops working.
     * Closes connection to database.
     */
    @Override
    public void destroy() {
        try {
            for (Validator<User> validator : VALIDATORS.values()) {
                validator.close();
            }
        } catch (Exception e) {
            LOG.error(e.getStackTrace());
        }
    }

}
