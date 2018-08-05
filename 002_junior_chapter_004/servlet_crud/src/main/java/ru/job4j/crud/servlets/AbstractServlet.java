package ru.job4j.crud.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.logic.DatabaseValidator;
import ru.job4j.crud.logic.Validator;
import ru.job4j.crud.model.Credentials;
import ru.job4j.crud.model.Info;
import ru.job4j.crud.model.User;

import javax.servlet.http.HttpServlet;
import java.io.BufferedReader;
import java.io.IOException;

import static ru.job4j.crud.Constants.*;
import static ru.job4j.crud.model.Credentials.Role.USER;

/**
 * General User HttpServlet class. Holds methods and fields needed
 * for every servlet to make interaction with user through html.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public abstract class AbstractServlet extends HttpServlet {
    /**
     * Logic layer object - validator.
     */
    protected static final Validator<User> VALIDATOR = DatabaseValidator.getInstance();
    /**
     * Logic layer object - servlet actions dispatch.
     */
    protected static final ActionsDispatch DISPATCH = new ActionsDispatch(VALIDATOR).initialize();
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractServlet.class);
    /**
     * User object meaning "bad answer".
     */
    protected final User userBadAnswer = new User(
            -1, -1,
            new Credentials("none", "none", USER),
            new Info("none", "none", "none", "none")
    );


    /**
     * Is called when servlet stops working.
     * Closes connection to database.
     */
    @Override
    public void destroy() {
        try {
            VALIDATOR.close();
        } catch (Exception e) {
            LOG.error(e.getStackTrace());
        }
    }

    /**
     * Reads data from reader and returns it as a single object.
     *
     * @param reader Reader where to get data from.
     * @return Data from reader if reader != null, empty data if reader == null.
     * @throws IOException If an I/O error occurs.
     */
    protected String readerToString(final BufferedReader reader) throws IOException {
        final StringBuilder result = new StringBuilder();
        if (reader != null) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }

    /**
     * Returns Credentials object from JsonNode.
     *
     * @param node JsonNode.
     * @return Credentials object.
     */
    protected Credentials formCredentials(JsonNode node) {
        return new Credentials(
                node.get(PARAM_USER_LOGIN.v()).asText(),
                node.get(PARAM_USER_PASSWORD.v()).asText(),
                Credentials.Role.valueOf(node.get(PARAM_USER_ROLE.v()).asText())
        );
    }

    /**
     * Returns Info object from JsonNode.
     *
     * @param node JsonNode.
     * @return Info object.
     */
    protected Info formInfo(JsonNode node) {
        return new Info(
                node.get(PARAM_USER_NAME.v()).asText(),
                node.get(PARAM_USER_EMAIL.v()).asText(),
                node.get(PARAM_USER_COUNTRY.v()).asText(),
                node.get(PARAM_USER_CITY.v()).asText()
        );
    }

}
