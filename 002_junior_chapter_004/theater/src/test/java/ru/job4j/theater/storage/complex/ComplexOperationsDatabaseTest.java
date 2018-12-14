package ru.job4j.theater.storage.complex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.model.Seat;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.theater.storage.database.DatabaseApi;
import ru.job4j.theater.storage.repository.account.AccountRepository;
import ru.job4j.theater.storage.repository.account.AccountRepositoryDatabase;
import ru.job4j.theater.storage.repository.account.AccountRepositoryDatabaseTest;
import ru.job4j.theater.storage.repository.payment.PaymentRepository;
import ru.job4j.theater.storage.repository.payment.PaymentRepositoryDatabase;
import ru.job4j.theater.storage.repository.seat.SeatRepository;
import ru.job4j.theater.storage.repository.seat.SeatRepositoryDatabase;
import ru.job4j.util.methods.CommonUtils;

import java.sql.SQLException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ComplexOperationsDatabaseTest {

    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(AccountRepositoryDatabaseTest.class);

    static {
        try {
            Database.getInstance().dropAndRecreateStructure();
        } catch (SQLException e) {
            LOG.error(CommonUtils.describeThrowable(e));
        }
    }

    private final DatabaseApi database = Database.getInstance();

    private final ComplexOperations complex = ComplexOperationsDatabase.getInstance();

    private final SeatRepository seatRepository = SeatRepositoryDatabase.getInstance();
    private final AccountRepository accountRepository = AccountRepositoryDatabase.getInstance();
    private final PaymentRepository paymentRepository = PaymentRepositoryDatabase.getInstance();

    private final Account accountOne = new Account.Builder("acc_1", "123-456").build();
    private final Account accountTwo = new Account.Builder("acc_2", "777-666").build();

    private final Seat seatOne = new Seat.Builder(1, 1, 50).build();
    private final Seat seatTwo = new Seat.Builder(1, 2, 43).owner(this.accountOne).build();


    @Before
    public void clearStorage() throws SQLException {
        this.database.clearTables();
    }

    @Test
    public void whenBuySeatAndAccountNotPresentThenAddAccountAndOccupySeat() throws SQLException {
        this.seatRepository.add(this.seatOne);
        //
        int row = this.seatOne.getRow();
        int col = this.seatOne.getColumn();
        String name = this.accountOne.getName();
        String phone = this.accountOne.getPhone();
        //
        Seat before = this.seatRepository.getByPlace(row, col);
        boolean isBought = complex.buySeat(row, col, name, phone);
        Seat after = this.seatRepository.getByPlace(row, col);
        Payment expectedPayment = new Payment
                .Builder(this.seatOne.getPrice(), this.accountOne)
                .comment(String.format("Payed for seat (%s, %s)", this.seatOne.getRow(), this.seatOne.getColumn()))
                .build();
        //
        assertThat(isBought, is(true));
        assertThat(before.getOwner(), is(Account.getEmptyAccount()));
        assertThat(after.getOwner(), is(this.accountOne));
        assertThat(this.accountRepository.getByNamePhone(name, phone), is(this.accountOne));
        assertThat(this.paymentRepository.getAll().get(0), is(expectedPayment));
    }

    @Test
    public void whenBuySeatAndAccountIsPresentThenOccupySeat() throws SQLException {
        this.seatRepository.add(this.seatOne);
        this.accountRepository.add(this.accountOne);
        //
        int row = this.seatOne.getRow();
        int col = this.seatOne.getColumn();
        String name = this.accountOne.getName();
        String phone = this.accountOne.getPhone();
        //
        Seat before = this.seatRepository.getByPlace(row, col);
        boolean isBought = complex.buySeat(row, col, name, phone);
        Seat after = this.seatRepository.getByPlace(row, col);
        Payment expectedPayment = new Payment
                .Builder(this.seatOne.getPrice(), this.accountOne)
                .comment(String.format("Payed for seat (%s, %s)", this.seatOne.getRow(), this.seatOne.getColumn()))
                .build();
        //
        assertThat(isBought, is(true));
        assertThat(before.getOwner(), is(Account.getEmptyAccount()));
        assertThat(after.getOwner(), is(this.accountOne));
        assertThat(this.paymentRepository.getAll().get(0), is(expectedPayment));
    }

    @Test
    public void whenSeatIsNotFreeThenNoPaymentAndOwnerNotChanged() throws SQLException {
        this.seatRepository.add(this.seatTwo);
        this.accountRepository.add(this.accountOne);
        //
        int row = this.seatTwo.getRow();
        int col = this.seatTwo.getColumn();
        String name = this.accountTwo.getName();
        String phone = this.accountTwo.getPhone();
        //
        Seat before = this.seatRepository.getByPlace(row, col);
        boolean isBought = complex.buySeat(row, col, name, phone);
        Seat after = this.seatRepository.getByPlace(row, col);
        // check non-existence of payment
        boolean noPayment = false;
        try {
            //noinspection ResultOfMethodCallIgnored
            this.paymentRepository.getAll().get(0);
        } catch (IndexOutOfBoundsException e) {
            noPayment = true;
        }
        //
        assertThat(isBought, is(false));
        assertThat(before.getOwner(), is(this.accountOne));
        assertThat(after.getOwner(), is(this.accountOne));
        assertThat(noPayment, is(true));
    }
}