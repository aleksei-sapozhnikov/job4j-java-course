package ru.job4j.theater.storage.repository.payment;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PaymentRepositoryHashSetTest {

    private final PaymentRepository repository = PaymentRepositoryHashSet.getInstance();

    private final Account accountOne = new Account.Builder("acc_1", "123-456").build();
    private final Account accountTwo = new Account.Builder("acc_2", "987-654").build();

    private final Payment paymentOne = new Payment.Builder(50, this.accountOne).build();
    private final Payment paymentTwo = new Payment.Builder(120, this.accountOne).comment("from account one").build();
    private final Payment paymentThree = new Payment.Builder(10, this.accountTwo).build();

    @Before
    public void clearRepository() throws SQLException {
        this.repository.clear();
    }

    /**
     * Test add() and getAll()
     */
    @Test
    public void whenAddPaymentsThenGetAllReturnsThem() {
        this.repository.add(this.paymentOne);
        this.repository.add(this.paymentTwo);
        this.repository.add(this.paymentThree);
        List<Payment> result = this.repository.getAll();
        result.sort(Comparator.comparing(Payment::getAmount));
        List<Payment> expected = Arrays.asList(this.paymentThree, this.paymentOne, this.paymentTwo);
        assertThat(result, is(expected));
    }
}