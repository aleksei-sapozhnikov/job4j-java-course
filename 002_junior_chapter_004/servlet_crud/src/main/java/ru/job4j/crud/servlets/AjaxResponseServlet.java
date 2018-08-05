package ru.job4j.crud.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Takes ajax requests and responds to them.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class AjaxResponseServlet extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AjaxResponseServlet.class);
    /**
     * JSON mapper to convert objects to json and vice versa.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Handles "POST" http requests.
     * <p>
     * Processes JSON string, forms object and stores it into the storage.
     *
     * @param req  Http request.
     * @param resp Http response.
     * @throws IOException If some I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String name = this.readerToString(req.getReader());

        if (name.equals("countries")) {
            List<String> countries = VALIDATOR.findAllCountries();
            String json = this.mapper.writeValueAsString(countries);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        } else if (name.equals("cities")) {
            List<String> countries = VALIDATOR.findAllCities();
            String json = this.mapper.writeValueAsString(countries);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        } else {
            int id = Integer.valueOf(name);
            User user = VALIDATOR.findById(id);
            String json = this.mapper.writeValueAsString(user);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        }

    }
}
