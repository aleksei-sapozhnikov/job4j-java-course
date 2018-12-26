package ru.job4j.theater.storage.complex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import ru.job4j.util.database.DbExecutor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ComplexOperationsDatabaseTest {

    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(AccountRepositoryDatabaseTest.class);

    private final DatabaseApi databaseApi = Database.getInstance();

    private final ComplexOperations complex = ComplexOperationsDatabase.getInstance();

    private final SeatRepository seatRepository = SeatRepositoryDatabase.getInstance();
    private final AccountRepository accountRepository = AccountRepositoryDatabase.getInstance();
    private final PaymentRepository paymentRepository = PaymentRepositoryDatabase.getInstance();

    private final Account accountOne = new Account.Builder("acc_1", "123-456").build();
    private final Account accountTwo = new Account.Builder("acc_2", "777-666").build();

    private final Seat seatOne = new Seat.Builder(1, 1, 50).build();
    private final Seat seatTwo = new Seat.Builder(1, 2, 43).owner(this.accountOne).build();

    @Test
    public void whenBuySeatAndAccountNotPresentThenAddAccountAndOccupySeat() {
        try (DbExecutor executor = this.databaseApi.getExecutor()) {
            this.seatRepository.add(this.seatOne, executor);
            //
            int row = this.seatOne.getRow();
            int col = this.seatOne.getColumn();
            String name = this.accountOne.getName();
            String phone = this.accountOne.getPhone();
            //
            Seat before = this.seatRepository.getByPlace(row, col, executor);
            boolean isBought = complex.buySeat(row, col, name, phone, executor);
            Seat after = this.seatRepository.getByPlace(row, col, executor);
            Payment expectedPayment = new Payment
                    .Builder(this.seatOne.getPrice(), this.accountOne)
                    .comment(String.format("Payed for seat (%s, %s)", this.seatOne.getRow(), this.seatOne.getColumn()))
                    .build();
            //
            assertThat(isBought, is(true));
            assertThat(before.getOwner(), is(Account.getEmptyAccount()));
            assertThat(after.getOwner(), is(this.accountOne));
            assertThat(this.accountRepository.getByNamePhone(name, phone, executor), is(this.accountOne));
            assertThat(this.paymentRepository.getAll(executor).get(0), is(expectedPayment));
        }
    }

    @Test
    public void whenBuySeatAndAccountIsPresentThenOccupySeat() {
        try (DbExecutor executor = this.databaseApi.getExecutor()) {
            this.seatRepository.add(this.seatOne, executor);
            this.accountRepository.add(this.accountOne, executor);
            //
            int row = this.seatOne.getRow();
            int col = this.seatOne.getColumn();
            String name = this.accountOne.getName();
            String phone = this.accountOne.getPhone();
            //
            Seat before = this.seatRepository.getByPlace(row, col, executor);
            boolean isBought = complex.buySeat(row, col, name, phone, executor);
            Seat after = this.seatRepository.getByPlace(row, col, executor);
            Payment expectedPayment = new Payment
                    .Builder(this.seatOne.getPrice(), this.accountOne)
                    .comment(String.format("Payed for seat (%s, %s)", this.seatOne.getRow(), this.seatOne.getColumn()))
                    .build();
            //
            assertThat(isBought, is(true));
            assertThat(before.getOwner(), is(Account.getEmptyAccount()));
            assertThat(after.getOwner(), is(this.accountOne));
            assertThat(this.paymentRepository.getAll(executor).get(0), is(expectedPayment));
        }
    }

    @Test
    public void whenSeatIsNotFreeThenNoPaymentAndOwnerNotChanged() {
        try (DbExecutor executor = this.databaseApi.getExecutor()) {
            this.seatRepository.add(this.seatTwo, executor);
            this.accountRepository.add(this.accountOne, executor);
            //
            int row = this.seatTwo.getRow();
            int col = this.seatTwo.getColumn();
            String name = this.accountTwo.getName();
            String phone = this.accountTwo.getPhone();
            //
            Seat before = this.seatRepository.getByPlace(row, col, executor);
            boolean isBought = complex.buySeat(row, col, name, phone, executor);
            Seat after = this.seatRepository.getByPlace(row, col, executor);
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
}