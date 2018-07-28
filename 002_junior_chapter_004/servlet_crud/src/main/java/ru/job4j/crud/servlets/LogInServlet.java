package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ru.job4j.crud.Constants.*;

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
        req.getRequestDispatcher(JSP_VIEWS_DIR.v().concat(JSP_LOGIN_PAGE.v())).forward(req, resp);
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
        String login = req.getParameter(PARAM_USER_LOGIN.v());
        String password = req.getParameter(PARAM_USER_PASSWORD.v());
        User user = VALIDATOR.findByCredentials(login, password);
        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute(PARAM_URI_CONTEXT_PATH.v(), req.getContextPath());
            session.setAttribute(PARAM_LOGGED_USER.v(), user);
            session.setAttribute(PARAM_URI_CREATE_USER.v(), URI_CREATE_USER.v());
            session.setAttribute(PARAM_URI_UPDATE_USER.v(), URI_UPDATE_USER.v());
            session.setAttribute(PARAM_URI_DELETE_USER.v(), URI_DELETE_USER.v());
            session.setAttribute(PARAM_URI_LOGIN.v(), URI_LOGIN.v());
            session.setAttribute(PARAM_URI_LOGOUT.v(), URI_LOGOUT.v());
            resp.sendRedirect(req.getContextPath());
        } else {
            req.setAttribute(PARAM_ERROR.v(), "Invalid credentials");
            this.doGet(req, resp);
        }
    }
}
    