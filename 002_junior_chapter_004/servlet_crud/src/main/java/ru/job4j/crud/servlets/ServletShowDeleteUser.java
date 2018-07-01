package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.User;
import ru.job4j.crud.logic.Validator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
     * @throws ServletException General exception a servlet can throw when it encounters difficulty.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String store = req.getParameter(this.getUrlParamStore());
        Validator<User> logic = this.getValidators().get(store);
        if (logic != null) {
            HttpSession session = req.getSession(false);
            synchronized (session) {
                if (session == null || session.getAttribute("login") == null) {
                    resp.sendRedirect(String.format("%s?%s",
                            String.join("/", req.getContextPath(), "login"),
                            String.join("=", this.getUrlParamStore(), store)
                    ));
                } else {
                    req.setAttribute("users", logic.findAll());
                    req.getRequestDispatcher(String.join("/", this.getViewsDir(), "list.jsp")).forward(req, resp);
                }
            }
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
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String store = req.getParameter(this.getUrlParamStore());
        DispatchServletActions dispatch = this.getDispatchers().get(store);
        if (dispatch != null) {
            dispatch.handle("delete", req, resp);
            resp.sendRedirect(String.join("/",
                    req.getContextPath(), String.format("list?store=%s", store)));
        } else {
            LOG.error(String.format("Unknown \"%s\" parameter, going to main page", this.getUrlParamStore()));
            resp.sendRedirect(req.getContextPath());
        }
    }

}
