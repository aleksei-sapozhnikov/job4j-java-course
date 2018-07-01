package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.User;
import ru.job4j.crud.logic.Validator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * General class for a presentation layer "update" servlet.
 * Shows form to update user fields and updates them.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class ServletUpdateUser extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(ServletUpdateUser.class);

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
        Integer id = Integer.valueOf(req.getParameter("id"));
        String store = req.getParameter(this.getUrlParamStore());
        Validator<User> logic = this.getValidators().get(store);
        User user = logic == null ? null : logic.findById(id);
        if (user != null) {
            req.setAttribute("user", user);
            req.getRequestDispatcher(String.join("/", this.getViewsDir(), "update.jsp")).forward(req, resp);
        } else {
            LOG.error(String.format("Unknown \"%s\" parameter, going to main page", this.getUrlParamStore()));
            resp.sendRedirect(req.getContextPath());
        }
    }

    /**
     * Handles POST requests. Does three actions: create/u[date/insert user.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     * @throws IOException Signals that an I/O exception of some sort has occurred.*
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String store = req.getParameter(this.getUrlParamStore());
        DispatchServletActions dispatch = this.getDispatchers().get(store);
        if (dispatch != null) {
            dispatch.handle("update", req, resp);
            resp.sendRedirect(String.join("/",
                    req.getContextPath(), String.format("list?store=%s", store)));
        } else {
            LOG.error(String.format("Unknown \"%s\" parameter, going to main page", this.getUrlParamStore()));
            resp.sendRedirect(req.getContextPath());
        }
    }


}
