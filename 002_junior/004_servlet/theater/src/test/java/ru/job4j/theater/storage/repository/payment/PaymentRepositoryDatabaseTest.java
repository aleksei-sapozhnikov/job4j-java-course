package ru.job4j.theater.storage.repository.payment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.theater.storage.database.DatabaseApi;
import ru.job4j.util.database.DbExecutor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PaymentRepositoryDatabaseTest {
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(PaymentRepositoryDatabaseTest.class);

    private final DatabaseApi databaseApi = Database.getInstance();
    private final PaymentRepository repository = PaymentRepositoryDatabase.getInstance();

    private final Account accountOne = new Account.Builder("acc_1", "123-456").build();
    private final Account accountTwo = new Account.Builder("acc_2", "987-654").build();

    private final Payment paymentOne = new Payment.Builder(50, this.accountOne).build();
    private final Payment paymentTwo = new Payment.Builder(120, this.accountOne).comment("from account one").build();
    private final Payment paymentThree = new Payment.Builder(1400, this.accountTwo).build();

    /**
     * Test add() and getAll()
     */
    @Test
    public void whenAddPaymentsThenGetAllReturnsThem() {
        try (DbExecutor executor = this.databaseApi.getExecutor()) {
            this.repository.add(this.paymentOne, executor);
            this.repository.add(this.paymentTwo, executor);
            this.repository.add(this.paymentThree, executor);
            List<Payment> result = this.repository.getAll(executor);
            result.sort(Comparator.comparing(Payment::getAmount));
            List<Payment> expected = Arrays.asList(this.paymentOne, this.paymentTwo, this.paymentThree);
            assertThat(result, is(expected));
        }


    }

    @Test
    public void whenAddPaymentWithOwnerThenGetEqualPayment() {
        try (DbExecutor executor = this.databaseApi.getExecutor()) {
            this.repository.add(this.paymentThree, executor);
            Payment found = this.repository.getAll(executor).get(0);
            assertThat(found == this.paymentThree, is(false));
            assertThat(found.getFrom() == this.accountTwo, is(false));
            assertThat(found, is(this.paymentThree));
            assertThat(found.getFrom(), is(this.accountTwo));
        }


    }
}