package ru.job4j.theater.storage.complex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.model.Seat;
import ru.job4j.theater.storage.DatabaseObjectUtils;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.theater.storage.database.DatabaseApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class making complex operations on objects in database - based repositories.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ComplexOperationsDatabase implements ComplexOperations {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(ComplexOperationsDatabase.class);

    /**
     * Database API.
     */
    private final DatabaseApi database = Database.getInstance();

    /**
     * Singleton instance
     */
    private static final ComplexOperationsDatabase INSTANCE = new ComplexOperationsDatabase();

    /**
     * Constructor.
     */
    private ComplexOperationsDatabase() {
    }

    /**
     * Returns INSTANCE.
     *
     * @return Value of INSTANCE field.
     */
    public static ComplexOperations getInstance() {
        return INSTANCE;
    }

    /**
     * Adds buyer to storage if not present, sets seat occupied by buyer, inserts payment for seat.
     *
     * @param row    Seat row.
     * @param column Seat column.
     * @param name   Buyer name.
     * @param phone  Buyer phone.
     */
    @Override
    public boolean buySeat(int row, int column, String name, String phone) throws SQLException {
        boolean result = false;
        try (Connection connection = this.database.getConnection();
             PreparedStatement addBuyer = connection
                     .prepareStatement(this.database.getQuery("sql.query.account.add"));
             PreparedStatement getSeat = connection
                     .prepareStatement(this.database.getQuery("sql.query.seat.get_by_place"));
             PreparedStatement occupySeat = connection
                     .prepareStatement(this.database.getQuery("sql.query.seat.update_by_place"));
             PreparedStatement addPayment = connection
                     .prepareStatement(this.database.getQuery("sql.query.payment.add"))
        ) {
            connection.setAutoCommit(false);
            try {
                Account buyer = new Account.Builder(name, phone).build();
                this.addBuyer(addBuyer, buyer);
                Seat found = this.findSeat(getSeat, row, column);
                Payment payment = this.createPaymentForSeat(found.getPrice(), buyer, row, column);
                if (found.isFree()) {
                    occupySeatInStorage(occupySeat, found, buyer);
                    addPayment(addPayment, payment);
                    result = true;
                }
                connection.commit();
            } catch (Throwable e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
        return result;
    }

    /**
     * Creates new Payment object which defines a payment for given seat.
     *
     * @param amount Payment amount.
     * @param from   Buyer account.
     * @param row    Seat row.
     * @param column Seat column.
     * @return Payment object.
     */
    private Payment createPaymentForSeat(int amount, Account from, int row, int column) {
        String payComment = String.format("Payed for seat (%s, %s)", row, column);
        return new Payment.Builder(amount, from).comment(payComment).build();
    }

    /**
     * Adds buyer account into storage.
     *
     * @param statement Statement adding into storage.
     * @param buyer     Buyer account.
     * @throws SQLException If problems with database occur.
     */
    private void addBuyer(PreparedStatement statement, Account buyer) throws SQLException {
        DatabaseObjectUtils.FillStatement.AccountStatements
                .fillAdd(statement, buyer);
        statement.execute();
    }

    /**
     * Returns Seat with given row and column from database as Seat object.
     *
     * @param statement Statement to get seat by place.
     * @param row       Seat row.
     * @param column    Seat column.
     * @return Seat object.
     * @throws SQLException If problems with database occur.
     */
    private Seat findSeat(PreparedStatement statement, int row, int column) throws SQLException {
        DatabaseObjectUtils.FillStatement.SeatStatements
                .fillGetByPlace(statement, row, column);
        Seat found = Seat.getEmptySeat();
        try (ResultSet res = statement.executeQuery()) {
            if (res.next()) {
                found = DatabaseObjectUtils.FormObject.formSeat(res);
            }
        }
        return found;
    }

    /**
     * Sets given seat database entry occupied by given account.
     *
     * @param statement Statement to update seat.
     * @param seat      Seat to occupy as Seat object.
     * @param account   Buyer account.
     * @throws SQLException If problems with database occur.
     */
    private void occupySeatInStorage(PreparedStatement statement, Seat seat, Account account) throws SQLException {
        DatabaseObjectUtils.FillStatement.SeatStatements
                .fillUpdateByPlace(statement, seat.getRow(), seat.getColumn(), seat.occupy(account));
        statement.execute();
    }

    /**
     * Adds payment object to database as entry.
     *
     * @param statement Statement to add payment.
     * @param payment   Object to add.
     * @throws SQLException If problems with database occur.
     */
    private void addPayment(PreparedStatement statement, Payment payment) throws SQLException {
        DatabaseObjectUtils.FillStatement.PaymentStatements
                .fillAdd(statement, payment);
        statement.execute();
    }


}
