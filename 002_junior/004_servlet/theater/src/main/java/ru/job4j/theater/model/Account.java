package ru.job4j.theater.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Account model.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Account {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Account.class);

    private static final Account EMPTY_ACCOUNT = new Account.Builder("empty name", "empty phone").build();

    /**
     * Account name.
     */
    private final String name;
    /**
     * Account phone.
     */
    private final String phone;

    /**
     * Initializes fields in the new objects.
     *
     * @param builder Builder object to gain values from.
     */
    private Account(Builder builder) {
        this.name = builder.name;
        this.phone = builder.phone;
    }

    /**
     * Returns EMPTY_ACCOUNT.
     *
     * @return Value of EMPTY_ACCOUNT field.
     */
    public static Account getEmptyAccount() {
        return EMPTY_ACCOUNT;
    }

    /**
     * Returns name.
     *
     * @return Value of name field.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns phone.
     *
     * @return Value of phone field.
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("Account[name='%s',phone='%s']", this.name, this.phone);
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
        Account account = (Account) o;
        return Objects.equals(name, account.name)
                && Objects.equals(phone, account.phone);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }

    /**
     * Builder class to construct Account objects.
     */
    public static class Builder {
        private final String name;
        private final String phone;

        /**
         * Constructor. Takes must-have parameters.
         *
         * @param name  Account name.
         * @param phone Account phone.
         */
        public Builder(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        /**
         * Returns Account object with parameters taken from this builder.
         *
         * @return New Account object.
         */
        public Account build() {
            return new Account(this);
        }
    }
}
