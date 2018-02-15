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
     * Test value() method.
     */
    @Test
    public void whenValueThenValue() {
        Account acc = new Account("32-32-12", new BigDecimal(1200.34));
        BigDecimal result = acc.value();
        BigDecimal expected = new BigDecimal(1200.34);
        assertThat(result, is(expected));
    }

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
}