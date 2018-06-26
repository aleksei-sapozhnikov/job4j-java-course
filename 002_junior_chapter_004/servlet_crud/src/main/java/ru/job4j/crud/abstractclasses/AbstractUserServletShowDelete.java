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
 * General class for a presentation layer "show and delete" servlet.
 * Shows all users currently in store and deletes them.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public abstract class AbstractUserServletShowDelete extends AbstractUserServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractUserServletShowDelete.class);

    /**
     * Constructor to initiate needed fields.
     *
     * @param logic Logic layer class object.
     */
    protected AbstractUserServletShowDelete(Validator<User> logic) {
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
                this.htmlHead("All users list"),
                this.dispatch.handle("showAll", req, resp),
                this.htmlTail()
        );
        resp.setContentType(this.getResponceContentType());
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append(result);
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
        String result = this.uniteStrings(
                this.dispatch.handle("delete", req, resp),
                "<br><br>",
                this.dispatch.handle("showAll", req, resp)
        );
        resp.setContentType(this.getResponceContentType());
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append(result);
            writer.flush();
        }
    }

}
