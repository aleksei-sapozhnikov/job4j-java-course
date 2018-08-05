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
     * Handles POST requests. Creates new user in database or redirects with error if creation failed.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(req.getReader());
        Credentials credentials = this.formCredentials(node);
        Info info = this.formInfo(node);
        User user = new User(credentials, info);
        int id = VALIDATOR.add(user);
        User resultUser = id != -1 ? VALIDATOR.findById(id) : this.userBadAnswer;
        resp.setContentType("application/json");
        String jsonResp = mapper.writeValueAsString(resultUser);
        resp.getWriter().write(jsonResp);
    }


}
