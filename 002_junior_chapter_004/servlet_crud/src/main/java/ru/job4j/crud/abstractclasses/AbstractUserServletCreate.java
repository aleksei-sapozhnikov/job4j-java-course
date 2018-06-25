package ru.job4j.crud.abstractclasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.User;
import ru.job4j.crud.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * General class for a presentation layer "create" servlet.
 * Shows form to add user and creates new user in store.
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
        String result = this.uniteStrings(
                this.htmlHead("Create new user"),
                this.dispatch.handle("formCreate", req, resp),
                this.htmlTail()
        );
        resp.setContentType(this.getResponceContentType());
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append(result);
            writer.flush();
        }
    }

    /**
     * Handles POST requests. Does three actions: create/update/delete user.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String result = this.uniteStrings("<br>",
                this.htmlHead("User create result"),
                this.dispatch.handle("create", req, resp),
                "<br><br>",
                this.dispatch.handle("showAll", req, resp),
                this.htmlTail()
        );
        resp.setContentType(this.getResponceContentType());
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append(result);
            writer.flush();
        }
    }

}
