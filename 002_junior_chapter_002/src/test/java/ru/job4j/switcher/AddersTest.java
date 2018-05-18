package ru.job4j.switcher;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Adders class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 17.05.2018
 */
public class AddersTest {

    /**
     * Test perform()
     */
    @Test
    public void whenPerformWithPositiveValuesThenResultAsExpected() throws InterruptedException {
        StringHolder holder = new StringHolder();
        Adders adders = new Adders(holder, 1, 2, 2, 3, 3);
        adders.perform();
        assertThat(holder.toString(), is("112221122211222"));
    }

    @Test
    public void whenPerformWithFirstNegativeValueThenResultAsExpected() throws InterruptedException {
        StringHolder holder = new StringHolder();
        Adders adders = new Adders(holder, -1, 5, 3, 1, 2);
        adders.perform();
        assertThat(holder.toString(), is("-1-1-1-1-13-1-1-1-1-13"));
    }

    @Test
    public void whenPerformWithSecondNegativeValueThenResultAsExpected() throws InterruptedException {
        StringHolder holder = new StringHolder();
        Adders adders = new Adders(holder, 10, 1, -3, 2, 1);
        adders.perform();
        assertThat(holder.toString(), is("10-3-3"));
    }

    @Test
    public void whenPerformWithBothNegativeValuesThenResultAsExpected() throws InterruptedException {
        StringHolder holder = new StringHolder();
        Adders adders = new Adders(holder, -92, 2, -5, 1, 3);
        adders.perform();
        assertThat(holder.toString(), is("-92-92-5-92-92-5-92-92-5"));
    }

    @Test
    public void whenCyclesZeroThenEmptyHolderString() throws InterruptedException {
        StringHolder holder = new StringHolder();
        Adders adders = new Adders(holder, 94934, 243, 343, 134, 0);
        adders.perform();
        assertThat(holder.toString(), is(""));
    }

    /**
     * Test exceptions throwing when parameters are invalid.
     */
    @Test(expected = RuntimeException.class)
    public void whenTimesFirstNegativeThenRuntimeException() {
        new Adders(new StringHolder(), 1, -2, 2, 1, 3);
    }

    @Test(expected = RuntimeException.class)
    public void whenTimesSecondNegativeThenRuntimeException() {
        new Adders(new StringHolder(), 1, 5, 2, -1, 3);
    }

    @Test(expected = RuntimeException.class)
    public void whenBothTimesNegativeThenRuntimeException() {
        new Adders(new StringHolder(), 1, -5, 2, -4, 3);
    }

    @Test(expected = RuntimeException.class)
    public void whenNumberOfCyclesNegativeThenRuntimeException() {
        new Adders(new StringHolder(), 1, 5, 2, 4, -3);
    }

    @Test(expected = RuntimeException.class)
    public void whenBothTimesAndCyclesNegativeThenRuntimeException() {
        new Adders(new StringHolder(), 1, -5, 2, -4, -3);
    }
}