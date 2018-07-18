package ru.job4j.crud.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static ru.job4j.crud.Constants.PARAM_ERROR;

public class DeleteUserServlet extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DeleteUserServlet.class);

    /**
     * Handles POST requests. Does three actions: create/u[date/insert user.
     *
     * @param req  Object that contains the request the client has made of the servlet.
     * @param resp Object that contains the response the servlet sends to the client.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String id = this.readerToString(req.getReader());
        String result;
        User deleted = VALIDATOR.delete(Integer.valueOf(id));
        if (deleted != null) {
            result = mapper.writeValueAsString(deleted);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put(PARAM_ERROR.v(), "Forbidden by validator: could not delete the user from database");
            result = mapper.writeValueAsString(error);
        }
        resp.setContentType("application/json");
        resp.getWriter().write(result);
    }
}
