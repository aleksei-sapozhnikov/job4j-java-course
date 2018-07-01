package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Presentation layer "create" servlet.
 * Shows form to add user and creates new user in store.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class ServletCreateUser extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(ServletCreateUser.class);

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
        req.getRequestDispatcher(String.join("/", this.getViewsDir(), "create.jsp")).forward(req, resp);
    }

    /**
     * Handles POST requests. Does three actions: create/update/delete user.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String store = req.getParameter(this.getUrlParamStore());
        DispatchServletActions dispatch = this.getDispatchers().get(store);
        if (dispatch != null) {
            dispatch.handle("create", req, resp);
            resp.sendRedirect(String.join("/",
                    req.getContextPath(), String.format("list?%s=%s", this.getUrlParamStore(), store)));
        } else {
            LOG.error(String.format("Unknown \"%s\" parameter, going to main page", this.getUrlParamStore()));
            resp.sendRedirect(req.getContextPath());
        }
    }

}
