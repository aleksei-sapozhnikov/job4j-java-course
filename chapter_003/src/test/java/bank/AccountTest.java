package bank;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Account class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class AccountTest {

    /**
     * Test requisites() method.
     */
    @Test
    public void whenRequisitesThenRequisites() {
        Account acc = new Account("32-32-12", new BigDecimal(1200.34));
        String result = acc.requisites();
        String expected = "32-32-12";
        assertThat(result, is(expected));
    }

    /**
     * Test equals() method.
     */
    @Test
    public void whenComparedToOtherClassThenFalse() {
        Account account = new Account("N-82", new BigDecimal("123.45"));
        String str = "Account";
        boolean result = account.equals(str);
        assertThat(result, is(false));
    }

    @Test
    public void whenComparedToNullThenFalse() {
        Account account = new Account("N-82", new BigDecimal("123.45"));
        String str = null;
        boolean result = account.equals(str);
        assertThat(result, is(false));
    }

    @Test
    public void whenEqualAccountsThenTrue() {
        Account left = new Account("N-82", new BigDecimal("123.45"));
        Account right = new Account("N-82", new BigDecimal("123.45"));
        boolean result = left.equals(right);
        assertThat(result, is(true));
    }

    @Test
    public void whenNotEqualAccountRequisitesThenFalse() {
        Account left = new Account("N-82", new BigDecimal("123.45"));
        Account right = new Account("G-64", new BigDecimal("123.45"));
        boolean result = left.equals(right);
        assertThat(result, is(false));
    }

    @Test
    public void whenSameRequisitesAndDifferentValuesThenTrue() {
        Account left = new Account("N-82", new BigDecimal("456.45"));
        Account right = new Account("N-82", new BigDecimal("123.45"));
        boolean result = left.equals(right);
        assertThat(result, is(true));
    }

    @Test
    public void whenComparedToItselfThenTrue() {
        Account account = new Account("N-82", new BigDecimal("456.45"));
        boolean result = account.equals(account);
        assertThat(result, is(true));
    }
}