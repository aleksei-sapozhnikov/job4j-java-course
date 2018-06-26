package ru.job4j.crud.abstractclasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.ActionsDispatch;
import ru.job4j.crud.User;
import ru.job4j.crud.Validator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * General User HttpServlet class. Holds methods and fields needed
 * for every servlet to make interaction with user through html.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class AbstractUserServlet extends HttpServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractUserServlet.class);
    /**
     * String for the HttpServletResponse.setContentType() method.
     */
    private static final String RESPONSE_CONTENT_TYPE = "text/html";
    /**
     * String format for every html page start tags.
     * <p>
     * Has parameter %s in title for String.format() method.
     */
    private static final String HTML_START = new StringBuilder()
            .append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">")
            .append("<html xmlns=\"http://www.w3.org/1999/xhtml\">")
            .append("<head>")
            .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=cp1251\" />")
            .append("</head>")
            .append("<title>")
            .append("%s")
            .append("</title>")
            .append("</head>")
            .append("<body>")
            .toString();
    /**
     * String format for every html page ending tags.
     */
    private static final String HTML_END = new StringBuilder()
            .append("</body>")
            .append("</html>")
            .toString();
    /**
     * Logic layer object making validation, adding/updating/deleting/etc. operations.
     */
    protected final Validator<User> logic;
    /**
     * Actions dispatch.
     */
    protected final ActionsDispatch dispatch;

    /**
     * Constructor to initiate needed fields.
     *
     * @param logic Logic layer class object.
     */
    protected AbstractUserServlet(Validator<User> logic) {
        this.logic = logic;
        this.dispatch = new ActionsDispatch(this.logic).init();
    }

    /**
     * Forms head elements for html page with given title.
     *
     * @param pageTitle Title for the page.
     * @return Head elements for html page.
     */
    protected String htmlHead(String pageTitle) {
        return String.format(HTML_START, pageTitle);
    }

    /**
     * Returns response html content type.
     *
     * @return Responce content type field value.
     */
    protected String getResponseContentType() {
        return RESPONSE_CONTENT_TYPE;
    }

    /**
     * Forms tail elements for html page.
     *
     * @return Tail html elements.
     */
    protected String htmlTail() {
        return HTML_END;
    }

    /**
     * Returns string uniting all given string elements
     * delimited by given string.
     *
     * @param elements Given string elements.
     * @return United string.
     */
    protected String uniteStrings(String... elements) {
        StringBuilder result = new StringBuilder();
        for (String element : elements) {
            result.append(element);
        }
        return result.toString();
    }

    /**
     * Is called when servlet stops working.
     * Closes connection to database.
     */
    @Override
    public void destroy() {
        try {
            this.logic.close();
        } catch (Exception e) {
            LOG.error(e.getStackTrace());
        }
    }

    /**
     * Returns context path for a storage in use now.
     * <p>
     * E.g.: general context path is "localhost:8080/crud".
     * Storage context path for the "database" storage is localhost:8080/crud/database
     * Storage context path for the "collection" storage is localhost:8080/crud/collection
     *
     * @param req Http servlet request to get context path from.
     * @return Storage context path.
     */
    protected String getStorageContextPath(HttpServletRequest req) {
        String context = req.getContextPath();
        String storage = req.getServletPath().split("/")[1];
        return String.join("/", context, storage);
    }
}
