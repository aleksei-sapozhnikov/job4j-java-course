package bank;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * User bank account.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class Account {

    /**
     * Money in this account.
     */
    private final BigDecimal value;

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
    public Account(String requisites, BigDecimal value) {
        this.requisites = requisites;
        this.value = value;
    }

    /**
     * Get value stored in the account.
     *
     * @return copy of the value field.
     */
    BigDecimal value() {
        return this.value;
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
        return Objects.equals(this.value, account.value)
                && Objects.equals(this.requisites, account.requisites);
    }

    /**
     * HashCode.
     *
     * @return integer hashCode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.requisites);
    }

    public String toString() {
        return "[req = " + this.requisites + ", value = " + this.value + "] ";
    }
}
