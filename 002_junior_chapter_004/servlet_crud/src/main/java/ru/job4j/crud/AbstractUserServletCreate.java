package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public abstract class AbstractUserServletCreate extends AbstractUserServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractUserServletCreate.class);

    /**
     * Constructor to initiate needed fields.
     *
     * @param logic Logic layer class object.
     */
    protected AbstractUserServletCreate(Validator<User> logic) {
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
        resp.setContentType("text/html");
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            //TODO: форму для создания нового польователя
            writer.append("Форма для нового пользователя");
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
        String result = this.dispatch.handle("create", req, resp);
        String users = this.dispatch.handle("showAll", req, resp);
        resp.setContentType("text/html");
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append(result);
            writer.append(users);
            writer.flush();
        }
    }

}
