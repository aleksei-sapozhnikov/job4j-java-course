package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Presentation layer servlet. Gets requests for actions:
 * create, update, delete user or show all users. Shows result.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public abstract class AbstractUserServlet extends HttpServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractUserServlet.class);
    /**
     * Logic layer to validate and add/update/delete/show all objects.
     */
    private final Validator<User> logic;
    /**
     * Actions dispatch.
     */
    private final ServletActionsDispatch dispatch;

    /**
     * Constructor to initiate needed fields.
     *
     * @param logic Logic layer class object.
     */
    protected AbstractUserServlet(Validator<User> logic) {
        this.logic = logic;
        this.dispatch = new ServletActionsDispatch(this.logic).init();
    }

    /**
     * Is called when servlet stops working.
     * Closes connection to database.
     */
    @Override
    public void destroy() {
        try {
            this.logic.close();
        } catch (Exception e) {
            LOG.error(e.getStackTrace());
        }
    }

    /**
     * Handles GET requests. Shows all users currently stored.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User[] users = this.logic.findAll();
        resp.setContentType("text/html");
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append("List of all users:").append(System.lineSeparator());
            for (User user : users) {
                writer.append(user.toString()).append(System.lineSeparator());
            }
            writer.flush();
        }
    }

    /**
     * Handles POST requests. Does three actions: create/u[date/insert user.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String result = this.dispatch.handle(req.getParameter("action"), req, resp);
        resp.setContentType("text/html");
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append(result);
            writer.flush();
        }
    }

}
