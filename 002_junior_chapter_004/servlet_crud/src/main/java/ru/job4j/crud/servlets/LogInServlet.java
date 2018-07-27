package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Presentation layer "login" servlet.
 * Shows form for user to enter his login/password and
 * saves the user into the http session.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class LogInServlet extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(LogInServlet.class);

    /**
     * Handles GET requests. Shows form for user to log in.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client
     * @throws ServletException General exception a servlet can throw when it encounters difficulty.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(VIEWS_DIR.concat(JSP_LOGIN_PAGE)).forward(req, resp);
    }

    /**
     * Handles POST requests. Adds user to the http session if user found in database.
     * Otherwise sends error message to client.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(PARAM_USER_LOGIN);
        String password = req.getParameter(PARAM_USER_PASSWORD);
        User user = VALIDATOR.findByCredentials(login, password);
        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute(PARAM_URI_CONTEXT_PATH, req.getContextPath());
            session.setAttribute(PARAM_LOGGED_USER, user);
            session.setAttribute(PARAM_URI_CREATE_USER, URI_CREATE_USER);
            session.setAttribute(PARAM_URI_UPDATE_USER, URI_UPDATE_USER);
            session.setAttribute(PARAM_URI_DELETE_USER, URI_DELETE_USER);
            session.setAttribute(PARAM_URI_LOGIN, URI_LOGIN);
            session.setAttribute(PARAM_URI_LOGOUT, URI_LOGOUT);
            resp.sendRedirect(req.getContextPath());
        } else {
            req.setAttribute(PARAM_ERROR, "Invalid credentials");
            this.doGet(req, resp);
        }
    }
}
    