package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.Credentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static ru.job4j.crud.Constants.*;
import static ru.job4j.crud.servlets.ActionsDispatch.Action.CREATE;

/**
 * Presentation layer "create" servlet.
 * Shows form to add user and creates new user in store.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class CreateUserServlet extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(CreateUserServlet.class);

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
        req.setAttribute(PARAM_ALL_ROLES.v(), Arrays.asList(Credentials.Role.values()));
        req.getRequestDispatcher(JSP_VIEWS_DIR.v().concat(JSP_CREATE_USER.v())).forward(req, resp);
    }

    /**
     * Handles POST requests. Creates new user in database or redirects with error if creation failed.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        boolean success = DISPATCH.handle(CREATE, req, resp);
        if (success) {
            resp.sendRedirect(req.getContextPath());
        } else {
            req.setAttribute(PARAM_ERROR.v(), "Message from server: user CREATE failed");
            this.doGet(req, resp);
        }
    }
}
