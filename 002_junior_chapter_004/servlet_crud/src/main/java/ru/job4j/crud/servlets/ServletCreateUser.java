package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.ActionsDispatch;

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
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher(String.join("/", VIEWS_DIR, "create.jsp")).forward(req, resp);
    }

    /**
     * Handles POST requests. Does three actions: create/update/delete user.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String store = req.getParameter("store");
        ActionsDispatch dispatch = ACT_DISPATCH.get(store);
        if (dispatch != null) {
            dispatch.handle("create", req, resp);
            resp.sendRedirect(String.join("/",
                    req.getContextPath(), String.format("list?store=%s", store)));
        } else {
            LOG.error("Unknown \"store\" parameter, going to main page");
            resp.sendRedirect(req.getContextPath());
        }
    }

}
