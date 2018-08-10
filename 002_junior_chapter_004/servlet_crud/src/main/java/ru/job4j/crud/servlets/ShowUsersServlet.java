package ru.job4j.crud.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.Credentials;
import ru.job4j.crud.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static ru.job4j.crud.Constants.*;

/**
 * General class for a presentation layer "show and delete" servlet.
 * Shows all users currently in store and deletes them.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class ShowUsersServlet extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(ShowUsersServlet.class);

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
        req.setAttribute(PARAM_ALL_USERS.v(), VALIDATOR.findAll());
        req.setAttribute(PARAM_ALL_ROLES.v(), Arrays.asList(Credentials.Role.values()));
        req.getRequestDispatcher(JSP_VIEWS_DIR.v().concat("/index.jsp")).forward(req, resp);
    }

    /**
     * Handles POST requests. Returns user object to given id (AJAX).
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     * @throws IOException Signals that an I/O exception of some sort has occurred.*
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(req.getReader());
        int id = node.get("id").asInt();
        User resultUser = VALIDATOR.findById(id);
        resp.setContentType("application/json");
        String jsonResp = mapper.writeValueAsString(resultUser);
        resp.getWriter().write(jsonResp);
    }
}
