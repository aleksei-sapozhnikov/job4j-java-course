package ru.job4j.theater.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Payment received.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Payment {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Payment.class);

    private static final Payment EMPTY_PAYMENT = new Payment.Builder(-1, Account.getEmptyAccount())
            .comment("empty payment").build();

    /**
     * Payment amount.
     */
    private final int amount;
    /**
     * Who made the payment.
     */
    private final Account from;
    /**
     * Comment to the payment.
     */
    private final String comment;

    /**
     * Initializes fields in the new objects.
     *
     * @param builder Builder object to gain values from.
     */
    private Payment(Builder builder) {
        this.amount = builder.amount;
        this.from = builder.from;
        this.comment = builder.comment;
    }

    /**
     * Returns EMPTY_PAYMENT.
     *
     * @return Value of EMPTY_PAYMENT field.
     */
    public static Payment getEmptyPayment() {
        return EMPTY_PAYMENT;
    }

    /**
     * Returns amount.
     *
     * @return Value of amount field.
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Returns from.
     *
     * @return Value of from field.
     */
    public Account getFrom() {
        return this.from;
    }

    /**
     * Returns comment.
     *
     * @return Value of comment field.
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("Payment[amount='%s',from='%s',comment='%s']", this.amount, this.from, this.comment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;
        return amount == payment.amount
                && Objects.equals(from, payment.from)
                && Objects.equals(comment, payment.comment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount, from, comment);
    }

    public static class Builder {
        private final int amount;
        private final Account from;
        private String comment = "unknown payment";

        /**
         * Constructor. Takes must-have parameters.
         *
         * @param amount Payment amount.
         * @param from   Who made the payment.
         */
        public Builder(int amount, Account from) {
            this.amount = amount;
            this.from = from;
        }

        /**
         * Sets value of the field to given one.
         *
         * @param val Value to set.
         * @return The Builder object.
         */
        public Builder comment(String val) {
            this.comment = val;
            return this;
        }

        /**
         * Returns Payment object with parameters taken from this builder.
         *
         * @return New Payment object.
         */
        public Payment build() {
            return new Payment(this);
        }
    }
}
