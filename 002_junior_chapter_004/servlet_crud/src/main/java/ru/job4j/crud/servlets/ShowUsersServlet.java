package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * General class for a presentation layer "show and delete" servlet.
 * Shows all users currently in store and deletes them.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class ShowUsersServlet extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(ShowUsersServlet.class);

    /**
     * Handles GET requests. Shows all users currently stored.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client
     * @throws ServletException General exception a servlet can throw when it encounters difficulty.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute(ALL_USERS_IN_SYSTEM, VALIDATOR.findAll());
        req.setAttribute(USER_LOGGED_IN_SESSION, req.getSession().getAttribute("loggedUser"));
        String url = String.join("/", this.getViewsDir(), "list.jsp");
        req.getRequestDispatcher(url).forward(req, resp);
    }
}
