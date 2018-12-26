package ru.job4j.crud.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static ru.job4j.crud.Constants.PARAM_ERROR;

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
        User user = this.formUser(node);
        int id = VALIDATOR.add(user);
        String result;
        if (id != -1) {
            User success = VALIDATOR.findById(id);
            result = mapper.writeValueAsString(success);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put(PARAM_ERROR.v(), "Forbidden by validator: could not add the user to database");
            result = mapper.writeValueAsString(error);
        }
        resp.setContentType("application/json");
        resp.getWriter().write(result);
    }


}
