package bank;

import java.math.BigDecimal;

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


}
