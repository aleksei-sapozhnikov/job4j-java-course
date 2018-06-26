package ru.job4j.crud.abstractclasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.User;
import ru.job4j.crud.Validator;

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
public abstract class AbstractUserServletUpdate extends AbstractUserServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractUserServletUpdate.class);

    /**
     * Constructor to initiate needed fields.
     *
     * @param logic Logic layer class object.
     */
    protected AbstractUserServletUpdate(Validator<User> logic) {
        super(logic);
    }

    /**
     * Handles GET requests. Shows all users currently stored.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(String.format("%s?%s",
                String.join("/", this.getStorageContextPath(req), "update.jsp"),
                String.join("&",
                        "id=".concat(req.getParameter("id"))
                )
        ));
    }

    /**
     * Handles POST requests. Does three actions: create/u[date/insert user.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.dispatch.handle("update", req, resp);
        resp.sendRedirect(this.getStorageContextPath(req));
    }

}
