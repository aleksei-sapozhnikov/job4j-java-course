package ru.job4j.theater.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class PaymentTest {

    private final Account accountOne = new Account.Builder("name_1", "123-456").build();
    private final Account accountTwo = new Account.Builder("name_2", "987-654").build();

    @Test
    public void testEmptyEntity() {
        Payment expected = new Payment.Builder(-1, Account.getEmptyAccount())
                .comment("empty payment")
                .build();
        assertThat(Payment.getEmptyPayment(), is(expected));
    }

    @Test
    public void testDefaultValues() {
        Payment payment = new Payment.Builder(-1, Account.getEmptyAccount()).build();
        assertThat(payment.getComment(), is("unknown payment"));
    }

    @Test
    public void testGetters() {
        Payment payment = new Payment.Builder(500, this.accountOne)
                .comment("payment_one")
                .build();
        assertThat(payment.getAmount(), is(500));
        assertThat(payment.getFrom(), is(this.accountOne));
        assertThat(payment.getComment(), is("payment_one"));
    }

    @Test
    public void testToString() {
        Payment payment = new Payment.Builder(400, this.accountOne)
                .comment("payment_one")
                .build();
        assertThat(payment.toString(), is(String
                .format("Payment[amount='400',from='%s',comment='payment_one']", this.accountOne)));
    }

    @Test
    public void testEqualsAndHashCode() {
        Payment main = new Payment.Builder(52, this.accountOne).build();
        Payment same = new Payment.Builder(52, this.accountOne).build();
        Payment amountOther = new Payment.Builder(41, this.accountOne).build();
        Payment fromOther = new Payment.Builder(52, this.accountTwo).build();
        Payment commentOther = new Payment.Builder(52, this.accountTwo).comment("other").build();
        assertThat(main == same, is(false));
        assertThat(main.equals(same), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
        assertThat(main.equals(amountOther), is(false));
        assertThat(main.equals(fromOther), is(false));
        assertThat(main.equals(commentOther), is(false));
    }
}