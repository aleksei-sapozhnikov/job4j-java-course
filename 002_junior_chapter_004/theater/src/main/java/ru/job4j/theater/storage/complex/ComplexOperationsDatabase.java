package ru.job4j.theater.storage.complex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.model.Seat;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.theater.storage.database.DatabaseApi;
import ru.job4j.theater.storage.repository.account.AccountRepository;
import ru.job4j.theater.storage.repository.account.AccountRepositoryDatabase;
import ru.job4j.theater.storage.repository.payment.PaymentRepository;
import ru.job4j.theater.storage.repository.payment.PaymentRepositoryDatabase;
import ru.job4j.theater.storage.repository.seat.SeatRepository;
import ru.job4j.theater.storage.repository.seat.SeatRepositoryDatabase;
import ru.job4j.util.database.DbExecutor;

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
     * Singleton instance
     */
    private static final ComplexOperationsDatabase INSTANCE = new ComplexOperationsDatabase();
    /**
     * Database API.
     */
    private final DatabaseApi database = Database.getInstance();
    /**
     * Account repository (owners).
     */
    private final AccountRepository accountRepository = AccountRepositoryDatabase.getInstance();
    /**
     * Seat repository.
     */
    private final SeatRepository seatRepository = SeatRepositoryDatabase.getInstance();
    /**
     * Payment repository.
     */
    private final PaymentRepository paymentRepository = PaymentRepositoryDatabase.getInstance();

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
    public boolean buySeat(int row, int column, String name, String phone) {
        boolean result = false;
        try (DbExecutor executor = this.database.getExecutor()) {
            Account buyer = new Account.Builder(name, phone).build();
            this.accountRepository.add(buyer, executor);
            Seat seat = this.seatRepository.getByPlace(row, column);
            if (seat.isFree()) {
                this.seatRepository.updateByPlace(row, column, seat.occupy(buyer), executor);
                String payComment = String.format("Payed for seat (%s, %s)", row, column);
                Payment payment = new Payment.Builder(seat.getPrice(), buyer).comment(payComment).build();
                this.paymentRepository.add(payment, executor);
                result = true;
            }
            executor.commit();
        }
        return result;
    }
}
