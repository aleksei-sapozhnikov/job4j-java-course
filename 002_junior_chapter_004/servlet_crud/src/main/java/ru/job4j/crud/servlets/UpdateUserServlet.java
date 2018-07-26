package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.Credentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * General class for a presentation layer "update" servlet.
 * Shows form to update user fields and updates them.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UpdateUserServlet extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UpdateUserServlet.class);

    /**
     * Handles GET requests. Gets user to update and sends it to html page.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client
     * @throws ServletException General exception a servlet can throw when it encounters difficulty.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Integer id = Integer.valueOf(req.getParameter("id"));
        req.setAttribute("oldUser", VALIDATOR.findById(id));
        req.setAttribute(USER_LOGGED_IN_SESSION, req.getSession().getAttribute("loggedUser"));
        req.setAttribute("roles", Arrays.asList(Credentials.Role.values()));
        req.getRequestDispatcher(String.join("/", this.getViewsDir(), "update.jsp")).forward(req, resp);
    }

    /**
     * Handles POST requests. Does three actions: create/u[date/insert user.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     * @throws IOException Signals that an I/O exception of some sort has occurred.*
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        boolean success = DISPATCH.handle("update", req, resp);
        if (success) {
            resp.sendRedirect(req.getContextPath());
        } else {
            req.setAttribute("error", "Message from server: user UPDATE failed");
            this.doGet(req, resp);
        }
    }

}