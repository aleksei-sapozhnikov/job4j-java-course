package ru.job4j.crud.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Takes ajax requests and responds to them.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class FillSelectorsServlet extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(FillSelectorsServlet.class);
    /**
     * Map connecting string requests with enums.
     */
    private final Map<String, Function<String, String>> dispatch = new HashMap<>();

    /**
     * Constructor for a new object.
     */
    public FillSelectorsServlet() {
        this.initDispatch();
    }

    /**
     * Initiates dispatcher - puts values to dispatch map.
     */
    private void initDispatch() {
        this.dispatch.put("ids", this.toIds());
        this.dispatch.put("countries", this.toCountries());
        this.dispatch.put("cities", this.toCities());
    }

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
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(req.getReader());
        String actionString = node.get("request").asText();
        Function<String, String> action = this.dispatch.get(actionString);
        String result = action != null ? action.apply("") : "";
        resp.setContentType("application/json");
        resp.getWriter().write(result);
    }

    /**
     * Handles request to return list of user id-s.
     *
     * @return JSON string with all user id-s.
     */
    private Function<String, String> toIds() {
        return (arg) -> {
            String result = "";
            try {
                List<User> users = VALIDATOR.findAll();
                List<Integer> ids = new ArrayList<>();
                for (User user : users) {
                    ids.add(user.getId());
                }
                result = new ObjectMapper().writeValueAsString(ids);
            } catch (JsonProcessingException e) {
                LOG.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
            }
            return result;
        };
    }

    /**
     * Handles request to return list of countries.
     *
     * @return JSON string with all countries.
     */
    private Function<String, String> toCountries() {
        return (arg) -> {
            String result = "";
            try {
                List<String> countries = VALIDATOR.findAllCountries();
                result = new ObjectMapper().writeValueAsString(countries);
            } catch (JsonProcessingException e) {
                LOG.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
            }
            return result;
        };
    }

    /**
     * Handles request to return list of cities.
     *
     * @return JSON string with all cities.
     */
    private Function<String, String> toCities() {
        return (arg) -> {
            String result = "";
            try {
                List<String> cities = VALIDATOR.findAllCities();
                result = new ObjectMapper().writeValueAsString(cities);
            } catch (JsonProcessingException e) {
                LOG.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
            }
            return result;
        };
    }


}
