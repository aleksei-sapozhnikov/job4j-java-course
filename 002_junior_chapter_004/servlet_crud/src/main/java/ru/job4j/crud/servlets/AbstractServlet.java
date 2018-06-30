package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.ActionsDispatch;
import ru.job4j.crud.User;
import ru.job4j.crud.Validator;
import ru.job4j.crud.collection.UserValidatorInCollection;
import ru.job4j.crud.database.UserValidatorInDatabase;

import javax.servlet.http.HttpServlet;
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
     * Main directory where views are stored.
     */
    protected static final String VIEWS_DIR = "/WEB-INF/views";
    /**
     * Map with user storage objects.
     */
    protected static final Map<String, Validator<User>> STORE_LOGIC = new HashMap<String, Validator<User>>() {
        {
            put("collection", UserValidatorInCollection.getInstance());
            put("database", UserValidatorInDatabase.getInstance());
        }
    };
    /**
     * Actions dispatch map.
     */
    protected static final Map<String, ActionsDispatch> ACT_DISPATCH = new HashMap<String, ActionsDispatch>() {
        {
            put("collection", new ActionsDispatch(UserValidatorInCollection.getInstance()).init());
            put("database", new ActionsDispatch(UserValidatorInDatabase.getInstance()).init());
        }
    };
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractServlet.class);

    /**
     * Is called when servlet stops working.
     * Closes connection to database.
     */
    @Override
    public void destroy() {
        try {
            for (Validator<User> logic : STORE_LOGIC.values()) {
                logic.close();
            }
        } catch (Exception e) {
            LOG.error(e.getStackTrace());
        }
    }

}
