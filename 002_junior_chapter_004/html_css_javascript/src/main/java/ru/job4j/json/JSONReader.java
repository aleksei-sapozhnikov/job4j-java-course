package ru.job4j.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Reads JSON object coming in http request and adds object to store.
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
    /**
     * Storage of users.
     */
    private final Storage<Human> storage = new Storage<>();
    /**
     * JSON mapper to convert objects to json and vice versa.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Handles "POST" htt requests.
     * <p>
     * Processes JSON string, forms object and stores it into the storage.
     *
     * @param req  Http request.
     * @param resp Http response.
     * @throws IOException If some I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String json = this.readerToString(req.getReader());
        Human human = this.mapper.readValue(json, Human.class);
        this.storage.add(human);
    }

    /**
     * Reads data from reader and returns it as a single object.
     *
     * @param reader Reader where to get data from.
     * @return Data from reader if reader != null, empty data if reader == null.
     * @throws IOException If an I/O error occurs.
     */
    private String readerToString(final BufferedReader reader) throws IOException {
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
