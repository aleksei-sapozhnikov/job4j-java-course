package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.ActionsDispatch;
import ru.job4j.crud.User;
import ru.job4j.crud.Validator;

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
public class ServletShowDeleteUser extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(ServletShowDeleteUser.class);

    /**
     * Handles GET requests. Shows all users currently stored.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String store = req.getParameter("store");
        Validator<User> logic = STORE_LOGIC.get(store);
        if (logic != null) {
            req.setAttribute("users", logic.findAll());
            req.getRequestDispatcher(String.join("/", VIEWS_DIR, "list.jsp")).forward(req, resp);
        } else {
            LOG.error("Unknown \"store\" parameter, going to main page");
            resp.sendRedirect(req.getContextPath());
        }
    }

    /**
     * Handles POST requests. Does three actions: create/u[date/insert user.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String store = req.getParameter("store");
        ActionsDispatch dispatch = ACT_DISPATCH.get(store);
        if (dispatch != null) {
            dispatch.handle("delete", req, resp);
            resp.sendRedirect(String.join("/",
                    req.getContextPath(), String.format("list?store=%s", store)));
        } else {
            LOG.error("Unknown \"store\" parameter, going to main page");
            resp.sendRedirect(req.getContextPath());
        }
    }

}
