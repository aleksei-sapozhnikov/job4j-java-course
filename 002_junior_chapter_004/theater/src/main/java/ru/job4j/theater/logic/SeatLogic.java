package ru.job4j.theater.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Seat;
import ru.job4j.theater.storage.complex.ComplexOperations;
import ru.job4j.theater.storage.complex.ComplexOperationsDatabase;
import ru.job4j.theater.storage.repository.seat.SeatRepository;
import ru.job4j.theater.storage.repository.seat.SeatRepositoryDatabase;
import ru.job4j.util.methods.CommonUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Operates Seat objects.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class SeatLogic extends HttpServlet {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(SeatLogic.class);

    private final ComplexOperations complexOperations = ComplexOperationsDatabase.getInstance();

    private final SeatRepository seats = SeatRepositoryDatabase.getInstance();

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Returns List of Lists of all seats in storage, grouped by rows.
     *
     * @param req  Request object.
     * @param resp Response object.
     * @throws IOException If problems with response writing occur.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<List<Seat>> byRows = this.groupByRows(this.seats.getAll());
        String result = this.mapper.writeValueAsString(byRows);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        this.writeResultToResponse(resp, result);
    }

    /**
     * Buys seat given by parameters.
     *
     * @param req  Request object.
     * @param resp Response object.
     * @throws IOException If problems with response writing occur.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonNode node = this.mapper.readTree(req.getReader());
        int row = Integer.parseInt(node.get("seat_row").asText());
        int column = Integer.parseInt(node.get("seat_column").asText());
        String name = node.get("account_name").asText();
        String phone = node.get("account_phone").asText();
        //
        String result = buySeat(row, column, name, phone);
        this.writeResultToResponse(resp, result);
    }

    /**
     * Writes servlet method result to response.
     *
     * @param resp   Response object.
     * @param result Result to write.
     */
    private void writeResultToResponse(HttpServletResponse resp, String result) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(result);
            writer.flush();
        }
    }

    /**
     * Performs operations needed to buy seat and returns result.
     *
     * @param row    Seat row.
     * @param column Seat column.
     * @param name   Buyer name.
     * @param phone  Buyer phone.
     * @return Result as json object string.
     */
    private String buySeat(int row, int column, String name, String phone) {
        String result;
        try {
            boolean bought = this.complexOperations.buySeat(row, column, name, phone);
            result = bought
                    ? String.format("{\"%s\":\"%s\"}", "success", "")
                    : String.format("{\"%s\":\"%s\"}", "error", "Не удалось купить место. Возможно, оно уже занято.");
        } catch (SQLException e) {
            LOG.error(CommonUtils.describeThrowable(e));
            result = String.format("{\"%s\":\"%s\"}", "error", "Ошибка базы данных (исключение) в ходе выполнения покупки");
        }
        return result;
    }

    /**
     * Re-collects Seat objects grouping them by row.
     *
     * @param seats List of seats.
     * @return List of lists of Seat object, grouped by row.
     */
    private List<List<Seat>> groupByRows(List<Seat> seats) {
        return new ArrayList<>(
                seats.stream().sorted()
                        .collect(Collectors.groupingBy(Seat::getRow)).values()
        );
    }
}
