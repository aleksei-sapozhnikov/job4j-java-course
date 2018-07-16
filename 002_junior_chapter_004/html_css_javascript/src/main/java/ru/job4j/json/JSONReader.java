package ru.job4j.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * TODO: description
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class JSONReader extends HttpServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(JSONReader.class);

    private final Storage storage = new Storage();

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String input = this.getData(req.getReader());
        Human human = this.mapper.readValue(input, Human.class);
        this.storage.add(human);
        LOG.info(human.toString());
    }

    /**
     * Reads data from reader and returns it as a single object.
     *
     * @param reader Reader where to get data from.
     * @return Data from reader if reader != null, empty data if reader == null.
     * @throws IOException If an I/O error occurs.
     */
    private String getData(final BufferedReader reader) throws IOException {
        final StringBuilder result = new StringBuilder();
        if (reader != null) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }

}
