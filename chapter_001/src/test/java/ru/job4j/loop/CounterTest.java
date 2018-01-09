package ru.job4j.loop;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Counter class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 09.01.2018
 */
public class CounterTest {

    /**
     * Test add method.
     */
    @Test
    public void whenSumEvenNumbersFromOneToTenThenThirty() {
        Counter count = new Counter();
        int result = count.add(1, 10);
        int expected = 30;
        assertThat(result, is(expected));
    }

    @Test
    public void whenSumEvenNumbersFromMinusTenToTenThenZero() {
        Counter count = new Counter();
        int result = count.add(-10, 10);
        int expected = 0;
        assertThat(result, is(expected));
    }

    @Test
    public void whenSumEvenNumbersFromMinusTenToFourThenMinusTwentyFour() {
        Counter count = new Counter();
        int result = count.add(-10, 4);
        int expected = -24;
        assertThat(result, is(expected));
    }

    @Test
    public void whenSumEvenNumbersFromFiveToFiveThenZero() {
        Counter count = new Counter();
        int result = count.add(5, 5);
        int expected = 0;
        assertThat(result, is(expected));
    }

    @Test
    public void whenSumEvenNumbersFromEightToEightThenEight() {
        Counter count = new Counter();
        int result = count.add(8, 8);
        int expected = 8;
        assertThat(result, is(expected));
    }

    @Test
    public void whenSumEvenNumbersStartBiggerThanFinishThenZero() {
        Counter count = new Counter();
        int result = count.add(6, 2);
        int expected = 0;
        assertThat(result, is(expected));
    }

}

