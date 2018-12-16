package ru.job4j.theater.storage.complex;

import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.model.Seat;
import ru.job4j.theater.storage.repository.account.AccountRepository;
import ru.job4j.theater.storage.repository.account.AccountRepositoryHashSet;
import ru.job4j.theater.storage.repository.payment.PaymentRepository;
import ru.job4j.theater.storage.repository.payment.PaymentRepositoryHashSet;
import ru.job4j.theater.storage.repository.seat.SeatRepository;
import ru.job4j.theater.storage.repository.seat.SeatRepositoryHashSet;

import java.sql.SQLException;

/**
 * Class making complex operations on objects in hash set - based repositories.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ComplexOperationsHashSet implements ComplexOperations {

    /**
     * Singleton instance
     */
    private static final ComplexOperationsHashSet INSTANCE = new ComplexOperationsHashSet();
    private final SeatRepository seatRepository = SeatRepositoryHashSet.getInstance();
    private final AccountRepository accountRepository = AccountRepositoryHashSet.getInstance();
    private final PaymentRepository paymentRepository = PaymentRepositoryHashSet.getInstance();

    /**
     * Constructor.
     */
    private ComplexOperationsHashSet() {
    }

    /**
     * Returns INSTANCE.
     *
     * @return Value of INSTANCE field.
     */
    public static ComplexOperationsHashSet getInstance() {
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
        Seat seat = this.seatRepository.getByPlace(row, column);
        Account buyer = getOrAddBuyer(name, phone);
        String comment = String.format("Payed for seat (%s, %s)", row, column);
        Payment payment = new Payment.Builder(seat.getPrice(), buyer).comment(comment).build();
        boolean occupied = occupySeat(row, column, buyer);
        if (occupied) {
            this.paymentRepository.add(payment);
        }
        return occupied;
    }

    /**
     * Tries to occupy seat in storage by the account.
     *
     * @param row    Seat row.
     * @param column Seat column.
     * @param buyer  Account to occupy by.
     * @return <tt>true</tt> if occupied successfully, <tt>false</tt> if not.
     */
    private boolean occupySeat(int row, int column, Account buyer) {
        boolean result = false;
        synchronized (this.seatRepository) {
            Seat seat = this.seatRepository.getByPlace(row, column);
            if (seat.isFree()) {
                this.seatRepository.updateByPlace(row, column, seat.occupy(buyer));
                result = true;
            }
        }
        return result;
    }

    /**
     * Checks for buyer in storage and if not found adds one.
     *
     * @param name  Buyer name.
     * @param phone Buyer phone.
     * @return Buyer from storage or added buyer.
     */
    private Account getOrAddBuyer(String name, String phone) {
        Account buyer = this.accountRepository.getByNamePhone(name, phone);
        if (buyer == Account.getEmptyAccount()) {
            buyer = new Account.Builder(name, phone).build();
            this.accountRepository.add(buyer);
        }
        return buyer;
    }
}
