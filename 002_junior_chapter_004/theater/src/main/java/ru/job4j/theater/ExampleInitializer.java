package ru.job4j.theater;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Seat;
import ru.job4j.theater.storage.complex.ComplexOperations;
import ru.job4j.theater.storage.complex.ComplexOperationsDatabase;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.theater.storage.repository.seat.SeatRepository;
import ru.job4j.theater.storage.repository.seat.SeatRepositoryDatabase;

import java.sql.SQLException;

/**
 * Initializes simple database with seats to show how the application works.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ExampleInitializer {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(ExampleInitializer.class);

    private static final SeatRepository SEAT_REPOSITORY = SeatRepositoryDatabase.getInstance();
    private static final ComplexOperations COMPLEX_OPERATIONS = ComplexOperationsDatabase.getInstance();

    /**
     * Initializes simple example.
     *
     * @param args Application arguments.
     * @throws SQLException If problems with database occur.
     */
    public static void main(String[] args) throws SQLException {
        Database.getInstance().dropAndRecreateStructure();
        addFreeSeats();
        buySomeSeats();
    }

    /**
     * Adds empty seats.
     */
    private static void addFreeSeats() {
        int nRows = 6;
        int nCols = 5;
        int[] prices = new int[nRows];
        // fill prices somehow
        int price = 0;
        for (int i = prices.length - 1; i >= 0; i--) {
            price += 100 + 100 * (prices.length - 1 - i);
            prices[i] = price;
        }
        // fill seats
        for (int i = 1; i <= nRows; i++) {
            for (int j = 1; j <= nCols; j++) {
                SEAT_REPOSITORY.add(
                        new Seat.Builder(i, j, prices[i - 1]).build()
                );
            }
        }
    }

    /**
     * Buys some seats.
     *
     * @throws SQLException If problems with database occur.
     */
    private static void buySomeSeats() throws SQLException {
        COMPLEX_OPERATIONS.buySeat(1, 3, "Вася", "123-456");
        COMPLEX_OPERATIONS.buySeat(2, 2, "Иван Михайлович", "112-323");
        COMPLEX_OPERATIONS.buySeat(5, 1, "Маша", "777-222");
        COMPLEX_OPERATIONS.buySeat(5, 2, "Маша", "777-222");
        COMPLEX_OPERATIONS.buySeat(5, 3, "Маша", "777-222");
        COMPLEX_OPERATIONS.buySeat(5, 4, "Маша", "777-222");
    }
}
