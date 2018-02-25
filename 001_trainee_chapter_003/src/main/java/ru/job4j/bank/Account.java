package ru.job4j.bank;

import ru.job4j.bank.exceptions.UnablePerformOperationException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * User ru.job4j.bank account.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class Account {

    /**
     * Money in this account.
     */
    private BigDecimal value;

    /**
     * Information to identify account.
     */
    private final String requisites;

    /**
     * Constructor.
     *
     * @param requisites account requisites.
     * @param value      money stored in account.
     */
    Account(String requisites, BigDecimal value) {
        this.requisites = requisites;
        this.value = value;
    }

    /**
     * Get requisites.
     *
     * @return requisi
     */
    String requisites() {
        return this.requisites;
    }

    /**
     * Checks if value in account is as given.
     *
     * @param value given value.
     * @return true or false as given value equal actual or not.
     */
    boolean hasValue(BigDecimal value) {
        return value.compareTo(this.value) == 0;
    }

    /**
     * Transfer amount of money to another account if there is enough money to transfer.
     *
     * @param other  account to transfer money to.
     * @param amount amount of money to transfer.
     * @return true if transfer happened.
     * @throws UnablePerformOperationException if money in account is less then amount to transfer.
     */
    boolean transferToIfEnough(Account other, BigDecimal amount) throws UnablePerformOperationException {
        BigDecimal left = this.value.subtract(amount);
        if (left.signum() < 0) {
            throw new UnablePerformOperationException("Transfer from account: not enough money.");
        }
        this.value = left;
        other.value = other.value.add(amount);
        return true;
    }

    /**
     * Equals method.
     *
     * @param other object to compare to.
     * @return true or false as objects are equal or not.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Account account = (Account) other;
        return Objects.equals(this.requisites, account.requisites);
    }

    /**
     * HashCode.
     *
     * @return integer hashCode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.requisites);
    }
}
