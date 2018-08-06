package ru.job4j.crud.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.Credentials;
import ru.job4j.crud.model.Info;
import ru.job4j.crud.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static ru.job4j.crud.Constants.*;

/**
 * General class for a presentation layer "update" servlet.
 * Shows form to update user fields and updates them.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UpdateUserServlet extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UpdateUserServlet.class);

    /**
     * Handles GET requests. Gets user to update and sends it to html page.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client
     * @throws ServletException General exception a servlet can throw when it encounters difficulty.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Integer id = Integer.valueOf(req.getParameter(PARAM_USER_ID.v()));
        req.setAttribute(PARAM_USER.v(), VALIDATOR.findById(id));
        req.setAttribute(PARAM_ALL_ROLES.v(), Arrays.asList(Credentials.Role.values()));
        req.getRequestDispatcher(JSP_VIEWS_DIR.v().concat(JSP_UPDATE_USER.v())).forward(req, resp);
    }

    /**
     * Handles POST requests. Does three actions: create/u[date/insert user.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     * @throws IOException Signals that an I/O exception of some sort has occurred.*
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(req.getReader());
        int id = Integer.valueOf(node.get("id").asText());
        JsonNode update = node.get("update");
        Credentials credentials = this.formCredentials(update);
        Info info = this.formInfo(update);
        User updateUser = new User(credentials, info);
        boolean success = VALIDATOR.update(id, updateUser);
        User resultUser = success ? VALIDATOR.findById(id) : this.userBadAnswer;
        resp.setContentType("application/json");
        String jsonResp = mapper.writeValueAsString(resultUser);
        resp.getWriter().write(jsonResp);
    }

}