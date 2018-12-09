package ru.job4j.theater.storage.repository.payment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.util.common.Utils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PaymentRepositoryDatabaseTest {
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(PaymentRepositoryDatabaseTest.class);

    static {
        try {
            Database.getInstance().dropAndRecreateStructure();
        } catch (SQLException e) {
            LOG.error(Utils.describeThrowable(e));
        }
    }

    private final PaymentRepository repository = PaymentRepositoryDatabase.getInstance();

    private final Account accountOne = new Account.Builder("acc_1", "123-456").build();
    private final Account accountTwo = new Account.Builder("acc_2", "987-654").build();

    private final Payment paymentOne = new Payment.Builder(50, this.accountOne).build();
    private final Payment paymentTwo = new Payment.Builder(120, this.accountOne).comment("from account one").build();
    private final Payment paymentThree = new Payment.Builder(1400, this.accountTwo).build();


    @Before
    public void clearRepository() throws SQLException {
        this.repository.clear();
    }

    /**
     * Test add() and getAll()
     */
    @Test
    public void whenAddPaymentsThenGetAllReturnsThem() throws SQLException {
        this.repository.add(this.paymentOne);
        this.repository.add(this.paymentTwo);
        this.repository.add(this.paymentThree);
        List<Payment> result = this.repository.getAll();
        result.sort(Comparator.comparing(Payment::getAmount));
        List<Payment> expected = Arrays.asList(this.paymentOne, this.paymentTwo, this.paymentThree);
        assertThat(result, is(expected));
    }

    @Test
    public void whenAddPaymentWithOwnerThenGetEqualPayment() throws SQLException {
        this.repository.add(this.paymentThree);
        Payment found = this.repository.getAll().get(0);
        assertThat(found == this.paymentThree, is(false));
        assertThat(found.getFrom() == this.accountTwo, is(false));
        assertThat(found, is(this.paymentThree));
        assertThat(found.getFrom(), is(this.accountTwo));


    }
}